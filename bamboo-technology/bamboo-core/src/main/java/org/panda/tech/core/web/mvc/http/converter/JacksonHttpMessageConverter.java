package org.panda.tech.core.web.mvc.http.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.panda.bamboo.common.util.jackson.JacksonUtil;
import org.panda.bamboo.common.util.jackson.builder.TypedPropertyFilter;
import org.panda.tech.core.web.context.SpringWebContext;
import org.panda.tech.core.web.mvc.http.annotation.ResultFilter;
import org.panda.tech.core.web.mvc.servlet.mvc.method.HandlerMethodMapping;
import org.panda.tech.core.web.mvc.util.WebMvcUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 基于Jackson实现的HTTP消息JSON转换器
 */
@Component
public class JacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter implements InitializingBean {

    @Autowired
    private HandlerMethodMapping handlerMethodMapping;

    private final Map<String, ObjectMapper> mappers = new HashMap<>();
    private final ObjectMapper defaultInternalMapper = JacksonUtil.copyDefaultMapper();
    private final ObjectMapper defaultExternalMapper = JacksonUtil.copyDefaultMapper();

    public JacksonHttpMessageConverter() {
        super(JacksonUtil.copyNonConcreteClassedMapper()); // 默认映射器实际上作为读取器，针对非具化类型读取类型字段
        setDefaultCharset(StandardCharsets.UTF_8);
    }

    @Override
    public void afterPropertiesSet() {
    }

    private String getMapperKey(boolean internal, Method method) {
        return (internal ? "in:" : "ex:") + method.toString();
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        HttpServletRequest request = SpringWebContext.getRequest();
        if (request != null) {
            HandlerMethod handlerMethod = this.handlerMethodMapping.getHandlerMethod(request);
            if (handlerMethod != null) {
                Method method = handlerMethod.getMethod();
                boolean internal = WebMvcUtil.isInternalReq(request);
                ObjectMapper mapper = getMapper(internal, method);
                String json = mapper.writeValueAsString(object);
                Charset charset = Objects.requireNonNullElse(getDefaultCharset(), StandardCharsets.UTF_8);
                IOUtils.write(json, outputMessage.getBody(), charset.name());
                return;
            }
        }
        super.writeInternal(object, type, outputMessage);
    }

    private ObjectMapper getMapper(boolean internal, Method method) {
        String mapperKey = getMapperKey(internal, method);
        ObjectMapper mapper = this.mappers.get(mapperKey);
        if (mapper == null) {
            // 存在结果过滤设置，或需要生成对象类型字段，则需构建方法特定的映射器
            ResultFilter[] resultFilters = method.getAnnotationsByType(ResultFilter.class);
            if (ArrayUtils.isNotEmpty(resultFilters)) {
                mapper = buildMapper(method.getReturnType(), resultFilters);
                this.mappers.put(mapperKey, mapper);
            } else { // 取默认映射器
                mapper = internal ? this.defaultInternalMapper : this.defaultExternalMapper;
            }
        }
        return mapper;
    }

    private ObjectMapper buildMapper(Class<?> resultType, ResultFilter[] resultFilters) {
        TypedPropertyFilter filter = new TypedPropertyFilter();
        for (ResultFilter resultFilter : resultFilters) {
            Class<?> filteredType = resultFilter.type();
            if (filteredType == Object.class) {
                filteredType = resultType;
            }
            filter.addIncludedProperties(filteredType, resultFilter.included());
            filter.addExcludedProperties(filteredType, resultFilter.excluded());
        }
        // 被过滤的类型中如果不包含结果类型，则加入结果类型，以确保至少包含结果类型
        Class<?>[] filteredTypes = filter.getTypes();
        if (!ArrayUtils.contains(filteredTypes, resultType)) {
            filteredTypes = ArrayUtils.add(filteredTypes, resultType);
        }
        ObjectMapper mapper = JacksonUtil.buildMapper(filter, filteredTypes);
        return mapper;
    }

}
