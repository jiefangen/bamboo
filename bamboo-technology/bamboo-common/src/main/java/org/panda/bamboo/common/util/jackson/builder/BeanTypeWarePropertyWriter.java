package org.panda.bamboo.common.util.jackson.builder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.PropertyWriter;

import java.lang.annotation.Annotation;

/**
 * 可获取Bean类型的属性书写者
 */
public class BeanTypeWarePropertyWriter extends PropertyWriter {

    private static final long serialVersionUID = -5535880550460337313L;
    private PropertyWriter base;
    private Class<?> beanType;

    public BeanTypeWarePropertyWriter(PropertyWriter base, Class<?> beanType) {
        super(base);
        this.base = base;
        this.beanType = beanType;
    }

    public Class<?> getBeanType() {
        return this.beanType;
    }

    @Override
    public String getName() {
        return this.base.getName();
    }

    @Override
    public PropertyName getFullName() {
        return this.base.getFullName();
    }

    @Override
    public JavaType getType() {
        return this.base.getType();
    }

    @Override
    public PropertyName getWrapperName() {
        return this.base.getWrapperName();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this.base.getAnnotation(acls);
    }

    @Override
    public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
        return this.base.getContextAnnotation(acls);
    }

    @Override
    public AnnotatedMember getMember() {
        return this.base.getMember();
    }

    @Override
    public void serializeAsField(Object value, JsonGenerator jgen, SerializerProvider provider)
            throws Exception {
        this.base.serializeAsField(value, jgen, provider);
    }

    @Override
    public void serializeAsOmittedField(Object value, JsonGenerator jgen,
            SerializerProvider provider) throws Exception {
        this.base.serializeAsOmittedField(value, jgen, provider);
    }

    @Override
    public void serializeAsElement(Object value, JsonGenerator jgen, SerializerProvider provider)
            throws Exception {
        this.base.serializeAsElement(value, jgen, provider);
    }

    @Override
    public void serializeAsPlaceholder(Object value, JsonGenerator jgen,
            SerializerProvider provider) throws Exception {
        this.base.serializeAsPlaceholder(value, jgen, provider);
    }

    @Override
    public void depositSchemaProperty(JsonObjectFormatVisitor objectVisitor,
            SerializerProvider provider) throws JsonMappingException {
        this.base.depositSchemaProperty(objectVisitor, provider);
    }

    @Override
    @Deprecated
    public void depositSchemaProperty(ObjectNode propertiesNode, SerializerProvider provider)
            throws JsonMappingException {
        this.base.depositSchemaProperty(propertiesNode, provider);
    }

}
