package org.panda.tech.data.redis.serializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.bamboo.common.util.jackson.JacksonUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class BaseTypeJackson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    private static final String CLASS_PROPERTY_KEY = Strings.DOUBLE_QUOTES
            + JsonTypeInfo.Id.CLASS.getDefaultPropertyName() + Strings.DOUBLE_QUOTES + Strings.COLON;

    private final Class<T> baseType;
    private final ObjectMapper objectMapper;

    public BaseTypeJackson2JsonRedisSerializer(Class<T> baseType) {
        this.baseType = baseType;
        if (ClassUtil.isComplex(baseType)) {
            this.objectMapper = JacksonUtil.withClassProperty(JacksonUtil.DEFAULT_MAPPER,
                    clazz -> !this.baseType.equals(clazz) && this.baseType.isAssignableFrom(clazz));
        } else {
            this.objectMapper = JacksonUtil.DEFAULT_MAPPER;
        }
        // 禁止生成类名信息
        this.objectMapper.deactivateDefaultTyping();
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        try {
            return this.objectMapper.writeValueAsBytes(t);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (ArrayUtils.isNotEmpty(bytes)) {
            try {
                String content = new String(bytes);
                if (content.contains(CLASS_PROPERTY_KEY)) {
                    return this.objectMapper.readValue(content, new TypeReference<>() {
                    });
                } else {
                    return this.objectMapper.readValue(content, this.baseType);
                }
            } catch (Exception e) {
                LogUtil.error(getClass(), e);
            }
        }
        return null;
    }

    @Override
    public Class<?> getTargetType() {
        return this.baseType;
    }

}
