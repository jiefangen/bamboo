package org.panda.tech.core.web.mvc.view.exception.annotation;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.model.Model;
import org.springframework.boot.web.servlet.error.ErrorController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注方法的可解决异常处理结果视图
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResolvableExceptionResult {

    /**
     * 表示前一个页面的视图名称
     */
    public static final String PREV_VIEW = "prev:";

    /**
     * @return 结果视图名称，默认交给{@link ErrorController}处理，也可以使用{@link ResolvableExceptionResult#PREV_VIEW}
     *         返回上一页面
     */
    String value() default Strings.EMPTY;

    /**
     * @return 异常处理完毕后要生成校验规则的模型类型集
     */
    Class<? extends Model>[] validate() default {};

    /**
     * @return 返回按钮的跳转地址
     */
    String back() default Strings.EMPTY;
}
