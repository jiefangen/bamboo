package org.panda.tech.core.util.message;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.date.TemporalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.temporal.Temporal;
import java.util.Locale;

/**
 * 消息解决器
 */
@Component
public class MessageResolver {

    @Autowired
    private MessageSource messageSource;

    /**
     * 将消息码转换为占位符形式，以便于作为占位符形式的消息参数
     *
     * @param code 消息码
     * @return 占位符
     */
    public static String getPlaceholder(String code) {
        return Strings.PLACEHOLDER_PREFIX + code + Strings.PLACEHOLDER_SUFFIX;
    }

    public String resolveMessage(String code, Object... args) {
        return resolveMessage(code, null, args);
    }

    /**
     * 解析指定消息码得到消息文本
     *
     * @param code   消息码
     * @param locale 区域
     * @param args   异常参数
     * @return 消息文本
     */
    public String resolveMessage(String code, Locale locale, Object... args) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String[] argTexts = getArgTexts(args, locale);
        return this.messageSource.getMessage(code, argTexts, code, locale);
    }

    private String[] getArgTexts(Object[] args, Locale locale) {
        if (args == null) {
            return null;
        }
        String[] result = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
           if (arg instanceof Temporal) {
                result[i] = TemporalUtil.format((Temporal) arg);
            } else if (arg instanceof String) {
                String argString = (String) arg;
                if (argString.startsWith(Strings.PLACEHOLDER_PREFIX) && argString.endsWith(
                        Strings.PLACEHOLDER_SUFFIX)) {
                    String argCode = argString.substring(Strings.PLACEHOLDER_PREFIX.length(),
                            argString.length() - Strings.PLACEHOLDER_SUFFIX.length());
                    result[i] = this.messageSource.getMessage(argCode, null, argCode, locale);
                } else {
                    result[i] = argString;
                }
            } else if (arg != null) {
                result[i] = arg.toString();
            }
            if (result[i] == null) {
                result[i] = Strings.EMPTY;
            }
        }
        return result;
    }

}
