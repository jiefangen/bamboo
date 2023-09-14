package org.panda.service.doc.core.domain.factory.word;

import org.apache.commons.io.IOUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.core.domain.factory.word.helper.WordDocHelper;
import org.panda.service.doc.core.domain.factory.word.helper.WordDocxHelper;
import org.panda.service.doc.core.domain.document.DocModel;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Word文档
 */
public class WordDoc implements Word {

    @Override
    public String read(InputStream inputStream, String extension) {
        String content = Strings.EMPTY;
        try {
            if (DocConstants.WORD_DOC.equalsIgnoreCase(extension)) {
                WordDocHelper wordDocHelper = new WordDocHelper(inputStream);
                content = wordDocHelper.getText();
                wordDocHelper.close();
            } else {
                WordDocxHelper wordDocxHelper = new WordDocxHelper(inputStream);
                content = wordDocxHelper.getText();
                wordDocxHelper.close();
            }
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return content;
    }

    @Override
    public void create(OutputStream outputStream, DocModel docModel) {
    }

    @Override
    public void convert(InputStream inputStream, OutputStream outputStream, String extension) {
        try {
            if (DocConstants.WORD_DOC.equalsIgnoreCase(extension)) {
                WordDocxHelper wordDocxHelper = new WordDocxHelper(inputStream);
                wordDocxHelper.convertToHtml(outputStream);
                wordDocxHelper.close();
            } else {
                WordDocHelper wordDocHelper = new WordDocHelper(inputStream);
                wordDocHelper.convertToHtml(outputStream);
                wordDocHelper.close();
            }
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

}
