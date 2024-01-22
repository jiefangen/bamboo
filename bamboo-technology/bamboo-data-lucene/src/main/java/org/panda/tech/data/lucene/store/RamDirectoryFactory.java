package org.panda.tech.data.lucene.store;

import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于内存的存储目录工厂
 */
public class RamDirectoryFactory implements DirectoryFactory {

    private final Map<String, Directory> mapping = new HashMap<>();

    @Override
    public Directory getDirectory(String path) throws IOException {
        Directory directory = this.mapping.get(path);
        if (directory == null) {
            directory = new ByteBuffersDirectory();
            this.mapping.put(path, directory);
        }
        return directory;
    }

    @Override
    public long getSpaceSize(String path) {
        long size = 0;
        Directory directory = this.mapping.get(path);
        if (directory != null) {
            try {
                String[] names = directory.listAll();
                for (String name : names) {
                    size += directory.fileLength(name);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return size;
    }

}
