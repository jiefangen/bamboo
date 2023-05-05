package org.panda.bamboo.common.model.nature;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * 属性元数据
 */
public class PropertyMeta {

    private String name;
    private Class<?> type;
    private List<Annotation> annotations = new ArrayList<>();

    public PropertyMeta(String name, Class<?> type, Annotation... annotations) {
        this.name = name;
        this.type = type;
        addAnnotations(annotations);
    }

    public void addAnnotations(Annotation[] annotations) {
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation != null) {
                    this.annotations.add(annotation);
                }
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getType() {
        return this.type;
    }

    public List<Annotation> getAnnotations() {
        return this.annotations;
    }

    public boolean containsAnnotation(Class<? extends Annotation> annotationType) {
        for (Annotation annotation : this.annotations) {
            if (annotation.getClass() == annotationType) {
                return true;
            }
        }
        return false;
    }

}
