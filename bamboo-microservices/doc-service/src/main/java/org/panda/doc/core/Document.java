package org.panda.doc.core;

import java.io.InputStream;

public interface Document {

    Object imports(InputStream inputStream, String extension);

    void exports();

}
