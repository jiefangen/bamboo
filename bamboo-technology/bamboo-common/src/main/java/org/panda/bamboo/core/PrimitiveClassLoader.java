package org.panda.bamboo.core;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;

import java.util.HashMap;
import java.util.Map;

/**
 * 可加载原生类型的类加载器
 */
public class PrimitiveClassLoader extends ClassLoader {

    private static final Map<String, Class<?>> PRIMITIVE_CLASSES = new HashMap<>();
    private static final Map<String, Class<?>> WRAPPED_CLASSES = new HashMap<>();

    static {
        PRIMITIVE_CLASSES.put("void", void.class);
        PRIMITIVE_CLASSES.put("boolean", boolean.class);
        PRIMITIVE_CLASSES.put("byte", byte.class);
        PRIMITIVE_CLASSES.put("char", char.class);
        PRIMITIVE_CLASSES.put("short", short.class);
        PRIMITIVE_CLASSES.put("int", int.class);
        PRIMITIVE_CLASSES.put("long", long.class);
        PRIMITIVE_CLASSES.put("float", float.class);
        PRIMITIVE_CLASSES.put("double", double.class);

        WRAPPED_CLASSES.put("void", Void.class);
        WRAPPED_CLASSES.put("boolean", Boolean.class);
        WRAPPED_CLASSES.put("byte", Byte.class);
        WRAPPED_CLASSES.put("char", Character.class);
        WRAPPED_CLASSES.put("short", Short.class);
        WRAPPED_CLASSES.put("int", Integer.class);
        WRAPPED_CLASSES.put("long", Long.class);
        WRAPPED_CLASSES.put("float", Float.class);
        WRAPPED_CLASSES.put("double", Double.class);
    }

    private boolean wrapped;

    public PrimitiveClassLoader(ClassLoader parent, boolean wrapped) {
        super(parent);
        this.wrapped = wrapped;
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        if (StringUtils.isNotBlank(className) && !className.contains(Strings.DOT)) { // 不含句点的很可能为原生类型
            Class<?> clazz;
            if (this.wrapped) {
                clazz = WRAPPED_CLASSES.get(className);
            } else {
                clazz = PRIMITIVE_CLASSES.get(className);
            }
            if (clazz != null) {
                return clazz;
            }
        }
        return super.loadClass(className, resolve);
    }
}
