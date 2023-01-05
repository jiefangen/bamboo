package org.panda.core.aop;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.panda.common.constant.SystemConstants;
import org.panda.common.constant.annotation.ControllerWebLog;
import org.panda.common.domain.ResultConstant;
import org.panda.common.domain.ResultVO;
import org.panda.common.utils.TokenUtil;
import org.panda.modules.monitor.domain.ActionLog;
import org.panda.modules.monitor.service.async.LogAsyncService;
import org.panda.modules.system.domain.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口日志切面
 *
 * @author fangen
 * @since JDK 11 2022/5/6
 */
@Aspect
@Component
@Order(100)
public class WebLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);

    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    private static final String START_TIME = "startTime";

    private static final String REQUEST_PARAMS = "requestParams";

    @Autowired
    private LogAsyncService asyncService;

    @Pointcut("execution(* org.panda.modules..*.*(..))")
    public void webLog() {}

    @Before(value = "webLog()&&  @annotation(controllerWebLog)")
    public void doBefore(JoinPoint joinPoint, ControllerWebLog controllerWebLog) {
        // 开始时间。
        long startTime = System.currentTimeMillis();
        Map<String, Object> threadInfo = new HashMap<>();
        threadInfo.put(START_TIME, startTime);
        // 从request中获取参数信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ipAddress = request.getRemoteAddr();
        JSONObject requestJson = new JSONObject();
        requestJson.put("ipAddress", ipAddress);
        // 获取用户信息
        String token = request.getHeader(SystemConstants.AUTH_HEADER);
        requestJson.put("username", TokenUtil.getUsername(token));
        // 请求参数
        StringBuilder requestStr = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                requestStr.append(arg.toString());
            }
        }
        requestJson.put("requestStr", requestStr);
        threadInfo.put(REQUEST_PARAMS, requestJson);
        threadLocal.set(threadInfo);
        LOGGER.debug("{} starts calling: requestData={}", controllerWebLog.content(), threadInfo.get(REQUEST_PARAMS));
    }

    @AfterReturning(value = "webLog()&& @annotation(controllerWebLog)", returning = "res")
    public void doAfterReturning(ControllerWebLog controllerWebLog, Object res) {
        Map<String, Object> threadInfo = threadLocal.get();
        long startTime = (long) threadInfo.getOrDefault(START_TIME, System.currentTimeMillis());
        long takeTime = System.currentTimeMillis() - startTime;
        if (controllerWebLog.intoDb()) {
            ActionLog actionLog = new ActionLog();
            actionLog.setActionType(controllerWebLog.actionType().getValue());
            actionLog.setContent(controllerWebLog.content());
            actionLog.setOperatingTime(new Date(startTime));
            actionLog.setElapsedTime(takeTime);
            if (res instanceof ResultVO) {
                ResultVO result = (ResultVO) res;
                if (result.getCode() != ResultConstant.DEFAULT_SUCCESS_CODE) {
                    actionLog.setExceptionInfo(result.getMessage());
                }
                actionLog.setStatusCode(result.getCode());
            } else {
                actionLog.setStatusCode(ResultConstant.DEFAULT_SUCCESS_CODE);
            }
            intoDb(actionLog);
        }
        threadLocal.remove();
        LOGGER.debug("{} end call: elapsed time={}ms,result={}", controllerWebLog.content(), takeTime, res);
    }

    @AfterThrowing(value = "webLog()&&  @annotation(controllerWebLog)", throwing = "throwable")
    public void doAfterThrowing(ControllerWebLog controllerWebLog, Throwable throwable) {
        if (controllerWebLog.intoDb()) {
            ActionLog actionLog = new ActionLog();
            actionLog.setActionType(controllerWebLog.actionType().getValue());
            actionLog.setContent(controllerWebLog.content());

            Map<String, Object> threadInfo = threadLocal.get();
            long startTime = (long) threadInfo.getOrDefault(START_TIME, System.currentTimeMillis());
            actionLog.setOperatingTime(new Date(startTime));
            long takeTime = System.currentTimeMillis() - startTime;
            actionLog.setElapsedTime(takeTime);
            actionLog.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            actionLog.setExceptionInfo(throwable.toString());
            intoDb(actionLog);
        }
        threadLocal.remove();
        LOGGER.error("{} invoke exception,exception information: {}",controllerWebLog.content(), throwable);
    }

    private void intoDb(ActionLog actionLog) {
        Map<String, Object> threadInfo = threadLocal.get();
        Object requestParams = threadInfo.get(REQUEST_PARAMS);
        JSONObject requestJson = JSONObject.parseObject(requestParams.toString());
        actionLog.setIpAddress(requestJson.getString("ipAddress"));
        String username = requestJson.getString("username");
        if (StringUtils.isEmpty(username)) {
            Subject subject = SecurityUtils.getSubject();
            UserDTO user = (UserDTO) subject.getPrincipal();
            if (user != null) {
                actionLog.setUsername(user.getUsername());
            }
        } else {
            actionLog.setUsername(username);
        }
        // 异步写入数据库
        asyncService.intoLogDb(actionLog);
    }
}
