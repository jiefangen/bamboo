package org.panda.doc.core;

import java.io.IOException;
import java.io.InputStream;

public interface Document {

    String read(InputStream inputStream, String fileExtension) throws IOException;

    void convert();

}
