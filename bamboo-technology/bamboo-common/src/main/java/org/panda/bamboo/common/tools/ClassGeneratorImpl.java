package org.panda.bamboo.common.tools;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.clazz.ClassDefault;
import org.panda.bamboo.common.util.lang.CollectionUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 类生成器实现
 */
public class ClassGeneratorImpl implements ClassGenerator {

    private JavaCompiler compiler;
    private MemoryJavaFileManager fileManager;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public ClassGeneratorImpl() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdManager = this.compiler.getStandardFileManager(null, null,
                StandardCharsets.UTF_8);
        this.fileManager = new MemoryJavaFileManager(stdManager);
    }

    private Class<?> generateClass(Map<String, String> codes) {
        List<JavaFileObject> fileObjects = new ArrayList<>();
        String className = null;
        for (Map.Entry<String, String> entry : codes.entrySet()) {
            fileObjects
                    .add(this.fileManager.createSourceFileObject(entry.getKey(), entry.getValue()));
            className = entry.getKey();
        }
        JavaCompiler.CompilationTask task = this.compiler.getTask(null, this.fileManager, null,
                null, null, fileObjects);
        if (task.call()) {
            try {
                return this.fileManager.getClassLoader().loadClass(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    @Cacheable("GeneratedSimpleClass")
    public <T> Class<? extends T> generateSimple(Class<T> clazz) {
        if (isGeneratable(clazz)) {
            Map<String, String> codes = generateCode(clazz);
            if (this.logger.isDebugEnabled()) {
                codes.forEach((className, code) -> {
                    this.logger.debug(className);
                    this.logger.debug(code);
                });
            }
            return (Class<? extends T>) generateClass(codes);
        }
        return clazz;
    }

    private boolean isGeneratable(Class<?> clazz) {
        return clazz != void.class && !clazz.isPrimitive()
                && (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))
                && !Map.class.isAssignableFrom(clazz) && !Iterable.class.isAssignableFrom(clazz);
    }

    private Map<String, String> generateCode(Class<?> clazz) {
        Map<String, String> codes = new LinkedHashMap<>();

        String packageName = generatePackageName(clazz);
        String classSimpleName = generateClassSimpleName(clazz);
        StringBuilder code = new StringBuilder("package ").append(packageName)
                .append(Strings.SEMICOLON).append(Strings.ENTER); // 声明包名
        // 声明类名
        code.append("public class ").append(classSimpleName);
        if (clazz.isInterface()) {
            code.append(" implements ");
        } else {
            code.append(" extends ");
        }
        code.append(clazz.getName()).append(Strings.LEFT_BRACE).append(Strings.ENTER);
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (isGeneratable(method)) { // 抽象的方法才需要实现
                String methodName = method.getName();
                Class<?>[] argTypes = method.getParameterTypes();
                Class<?> returnType = method.getReturnType();
                if (argTypes.length == 0) {
                    String propertyName = null;
                    if (methodName.startsWith("get")) {
                        propertyName = methodName.substring(3);
                    } else if (methodName.startsWith("is") && returnType == boolean.class) {
                        propertyName = methodName.substring(2);
                    }
                    if (propertyName != null && Character.isUpperCase(propertyName.charAt(0))) {
                        propertyName = StringUtil.firstToLowerCase(propertyName);
                        String propertyClassName = getUsedClassName(returnType);
                        if (isGeneratable(returnType)) { // 属性类型需要生成，则一同生成
                            Map<String, String> propertyClassCodes = generateCode(returnType);
                            if (propertyClassCodes.size() > 0) {
                                codes.putAll(propertyClassCodes);
                                propertyClassName = CollectionUtil
                                        .getLast(propertyClassCodes.entrySet(), null).getKey();
                            }
                        }
                        // 属性
                        code.append("    private ").append(propertyClassName).append(Strings.SPACE)
                                .append(propertyName).append(Strings.SEMICOLON)
                                .append(Strings.ENTER);
                        // getter方法
                        code.append("    public ").append(propertyClassName).append(Strings.SPACE)
                                .append(methodName).append("(){\n").append("        return this.")
                                .append(propertyName).append(Strings.SEMICOLON)
                                .append(Strings.ENTER).append("    }").append(Strings.ENTER);
                        // setter方法
                        code.append("    public void set")
                                .append(StringUtil.firstToUpperCase(propertyName))
                                .append(Strings.LEFT_BRACKET).append(propertyClassName)
                                .append(Strings.SPACE).append(propertyName).append("){\n")
                                .append("        this.").append(propertyName).append(Strings.EQUAL)
                                .append(propertyName).append(Strings.SEMICOLON)
                                .append(Strings.ENTER).append("    }").append(Strings.ENTER);
                        continue;
                    }
                }
                // 不是属性getter方法，则生成方法的默认实现
                code.append("    public ").append(getUsedClassName(returnType))
                        .append(Strings.SPACE).append(methodName).append(Strings.LEFT_BRACKET);
                if (argTypes.length > 0) {
                    for (int i = 0; i < argTypes.length; i++) {
                        code.append(getUsedClassName(argTypes[i])).append(" arg").append(i)
                                .append(Strings.COMMA);
                    }
                    code.deleteCharAt(code.length() - 1);
                }
                code.append("){\n");
                if (returnType != void.class) {
                    code.append("        return ").append(ClassDefault.visit(returnType, true))
                            .append(Strings.SEMICOLON).append(Strings.ENTER);
                }
                code.append("    }\n");
            }
        }
        code.append(Strings.RIGHT_BRACE);
        String className = packageName + Strings.DOT + classSimpleName;
        codes.put(className, code.toString());
        return codes;
    }

    private boolean isGeneratable(Method method) {
        if (Modifier.isAbstract(method.getModifiers())) {
            String methodName = method.getName();
            Class<?>[] argTypes = method.getParameterTypes();
            Class<?> returnType = method.getReturnType();
            if ("toString".equals(methodName) && returnType == String.class
                    && argTypes.length == 0) {
                return false;
            }
            if ("hashCode".equals(methodName) && returnType == int.class && argTypes.length == 0) {
                return false;
            }
            if ("equals".equals(methodName) && returnType == boolean.class && argTypes.length == 1
                    && argTypes[0] == Object.class) {
                return false;
            }
            return true;
        }
        return false;
    }

    private String generatePackageName(Class<?> clazz) {
        return getClass().getPackageName() + ".temp." + clazz.getPackageName();
    }

    private String generateClassSimpleName(Class<?> clazz) {
        String originalClassName = clazz.getSimpleName();
        if (originalClassName.startsWith("Abstract")) {
            originalClassName = originalClassName.substring(8);
        }
        return "Simple" + originalClassName;
    }

    private String getUsedClassName(Class<?> usedClass) {
        if (usedClass == void.class || usedClass.isPrimitive()
                || usedClass.getPackageName().equals("java.lang")) {
            return usedClass.getSimpleName();
        }
        return usedClass.getName();
    }

}
