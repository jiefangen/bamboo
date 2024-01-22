package org.panda.tech.data.lucene.store;

import org.apache.lucene.store.Directory;

import java.io.IOException;

/**
 * 存储目录工厂
 */
public interface DirectoryFactory {

    Directory getDirectory(String path) throws IOException;

    /**
     * @param path 目录相对路径
     * @return 占用空间大小
     */
    long getSpaceSize(String path);

}
