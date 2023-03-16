package org.panda.doc.core.word;

import java.io.InputStream;

/**
 * Word文档
 */
public class WordDoc implements Word {

    @Override
    public String imports(InputStream inputStream, String fileExtension) {
        return null;
    }

    @Override
    public void exports() {
        // do something
    }

}
