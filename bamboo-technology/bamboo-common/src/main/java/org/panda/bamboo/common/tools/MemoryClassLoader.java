package org.panda.bamboo.common.tools;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

/**
 * 基于内存的类加载器
 */
public class MemoryClassLoader extends URLClassLoader {

    private Map<String, MemoryJavaFileManager.OutputMemoryJavaFileObject> fileObjects;

    public MemoryClassLoader(ClassLoader parent,
            Map<String, MemoryJavaFileManager.OutputMemoryJavaFileObject> fileObjects) {
        super(new URL[0], parent);
        this.fileObjects = fileObjects;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        MemoryJavaFileManager.OutputMemoryJavaFileObject fileObject = this.fileObjects.get(name);
        if (fileObject != null) {
            byte[] bytes = fileObject.getBytes();
            return defineClass(name, bytes, 0, bytes.length);
        }
        return super.findClass(name);
    }
}
