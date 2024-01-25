package org.panda.tech.data.redis.config;

import org.panda.bamboo.common.util.jackson.JacksonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * Redis模版配置支持，通用访问模版建议加载使用
 *
 * @author fangen
 **/
public abstract class RedisTemplateConfigSupport {

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // 配置key和value的序列化规则
        template.setKeySerializer(StringRedisSerializer.UTF_8);
        template.setValueSerializer(getValueSerializer());
        // 配置hashKey和hashValue的序列化规则
        template.setHashKeySerializer(StringRedisSerializer.UTF_8);
        template.setHashValueSerializer(getValueSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 1.OxmSerializer：以xml格式存储（但还是String类型~），解析起来也比较复杂，效率也比较低 - 不推荐
     * 2.JdkSerializationRedisSerializer：默认的序列化方式，对象都必须实现java.io.Serializable接口，比较笨重 - 不推荐
     * 3.StringRedisSerializer：轻量级，效率也比较高。StringRedisTemplate默认的序列化方式 - 推荐
     * 4.GenericToStringSerializer：需要调用者给传一个对象到字符串互转的Converter（相当于转换为字符串的操作交给转换器去做）- 不推荐
     * 5.Jackson2JsonRedisSerializer：优点速度快，不需要实现Serializable接口，缺点此类的构造函数中必须有类型参数 - 半推荐
     * 6.GenericJackson2JsonRedisSerializer：基本和上面的Jackson2JsonRedisSerializer功能差不多 - 推荐
     */
    protected RedisSerializer<Object> getValueSerializer() {
        // 使用GenericJackson2JsonRedisSerializer替换默认序列化（默认采用的是JDK序列化）
        return new GenericJackson2JsonRedisSerializer(JacksonUtil.DEFAULT_MAPPER);
    }

}
