package org.panda.bamboo.common.util.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.jackson.builder.TypedPropertyFilter;
import org.panda.bamboo.common.model.nature.FilteredNames;
import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.panda.bamboo.common.util.lang.CollectionUtil;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 *
 * @author fangen
 */
public class JsonUtil {

    private static String toJson(Object obj, PropertyFilter filter) {
        if (obj == null) {
            return null;
        }
        ObjectMapper mapper;
        if (filter != null) {
            mapper = JacksonUtil.buildMapper(filter, obj.getClass());
        } else {
            mapper = JacksonUtil.DEFAULT_MAPPER;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object obj, String... excludedProperties) {
        PropertyFilter filter = null;
        if (ArrayUtils.isNotEmpty(excludedProperties)) {
            filter = SimpleBeanPropertyFilter.serializeAllExcept(excludedProperties);
        }
        return toJson(obj, filter);
    }

    /**
     * 将指定任意类型的对象转换为JSON标准格式的字符串
     *
     * @param obj                对象
     * @param excludedType       需进行属性排除的类
     * @param excludedProperties 需排除的属性
     * @return JSON格式的字符串
     */
    public static String toJson(Object obj, Class<?> excludedType, String... excludedProperties) {
        TypedPropertyFilter filter = null;
        if (ArrayUtils.isNotEmpty(excludedProperties)) {
            filter = new TypedPropertyFilter().addExcludedProperties(excludedType, excludedProperties);
        }
        return toJson(obj, filter);
    }

    /**
     * 将指定任意类型的对象转换为JSON标准格式的字符串
     *
     * @param obj                   对象
     * @param filteredPropertiesMap 过滤属性映射集
     * @return JSON格式的字符串
     */
    public static String toJson(Object obj, Map<Class<?>, FilteredNames> filteredPropertiesMap) {
        TypedPropertyFilter filter = null;
        if (CollectionUtil.isNotEmpty(filteredPropertiesMap)) {
            filter = new TypedPropertyFilter().addAllProperties(filteredPropertiesMap);
        }
        return toJson(obj, filter);
    }

    public static String toJsonWithClassProperty(Object obj) {
        try {
            return JacksonUtil.CLASSED_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON标准形式的字符串转换为Map
     *
     * @param json JSON标准形式的字符串
     * @return 转换形成的Map
     */
    public static Map<String, Object> json2Map(String json) {
        if (json == null) {
            return null;
        }
        if (StringUtils.isBlank(json)) {
            return Collections.emptyMap();
        }
        try {
            return JacksonUtil.DEFAULT_MAPPER.readValue(json, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON标准形式的字符串转换为指定类型的对象
     *
     * @param json      JSON标准形式的字符串
     * @param beanClass 要转换的目标类型
     * @param <T>       目标类型
     * @return 转换形成的对象
     */
    public static <T> T json2Bean(String json, Class<T> beanClass) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return JacksonUtil.DEFAULT_MAPPER.readValue(json, beanClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON标准形式的字符串转换生成的对象的所有属性值覆盖指定对象的相应属性值
     *
     * @param json JSON标准形式的字符串
     * @param bean 要覆盖的对象
     */
    public static void jsonCoverBean(String json, Object bean) {
        Map<String, Object> map = json2Map(json);
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                BeanUtil.setPropertyValue(bean, entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 将JSON标准形式的字符串转换为具体类型不确定的List
     *
     * @param json JSON标准形式的字符串
     * @return 转换形成的对象List
     */
    public static List<Object> json2List(String json) {
        if (json == null) {
            return null;
        }
        if (StringUtils.isBlank(json)) {
            return Collections.emptyList();
        }
        try {
            return JacksonUtil.DEFAULT_MAPPER.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON标准形式的字符串转换为指定类型的对象List
     *
     * @param json          JSON标准形式的字符串
     * @param componentType 元素类型
     * @param <T>           目标元素类型
     * @return 转换形成的对象List
     */
    public static <T> List<T> json2List(String json, Class<T> componentType) {
        if (json == null) {
            return null;
        }
        if (StringUtils.isBlank(json)) {
            return Collections.emptyList();
        }
        CollectionType type = JacksonUtil.DEFAULT_MAPPER.getTypeFactory().constructCollectionType(List.class,
                componentType);
        try {
            return JacksonUtil.DEFAULT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON标准形式的字符串转换为数组
     *
     * @param json JSON标准形式的字符串
     * @return 转换形成的数组
     */
    public static Object[] json2Array(String json) {
        List<Object> list = json2List(json);
        if (list == null) {
            return null;
        }
        return list.toArray(new Object[0]);
    }

    /**
     * 将JSON标准形式的字符串转换为数组
     *
     * @param json          JSON标准形式的字符串
     * @param componentType 元素类型
     * @param <T>           目标元素类型
     * @return 转换形成的数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] json2Array(String json, Class<T> componentType) {
        List<T> list = json2List(json, componentType);
        if (list == null) {
            return null;
        }
        T[] array = (T[]) Array.newInstance(componentType, list.size());
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

}
