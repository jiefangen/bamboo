package org.panda.service.doc.core.domain.factory.pdf.helper;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.panda.bamboo.common.constant.file.FileExtensions;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.service.doc.common.DocExceptionCodes;
import org.panda.service.doc.core.domain.document.DocOutline;
import org.panda.service.doc.core.domain.document.DocOutlineItem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * PDF文档
 */
public class PdfDocHelper {

    private PDDocument origin;
    private PDFRenderer renderer;

    public PdfDocHelper(File file) {
        try {
            this.origin = PDDocument.load(file);
        } catch (IOException e) {
            throw new BusinessException(DocExceptionCodes.CAN_NOT_LOAD, FileExtensions.PDF.toUpperCase());
        }
    }

    public PdfDocHelper(InputStream in) {
        try {
            this.origin = PDDocument.load(in);
        } catch (IOException e) {
            throw new BusinessException(DocExceptionCodes.CAN_NOT_LOAD, FileExtensions.PDF.toUpperCase());
        }
    }

    public PDDocument getOrigin() {
        return this.origin;
    }

    public DocOutline getOutline() {
        DocOutline outline = new DocOutline();
        outline.setPageCount(this.origin.getNumberOfPages());
        List<DocOutlineItem> items = new ArrayList<>();
        PDDocumentOutline pdOutline = this.origin.getDocumentCatalog().getDocumentOutline();
        if (pdOutline != null) {
            PDOutlineItem outlineItem = pdOutline.getFirstChild();
            while (outlineItem != null) {
                items.add(toOutlineItem(outlineItem, 1));
                outlineItem = outlineItem.getNextSibling();
            }
        }
        outline.setItems(items);
        return outline;
    }

    private DocOutlineItem toOutlineItem(PDOutlineItem outlineItem, int level) {
        DocOutlineItem item = new DocOutlineItem();
        item.setLevel(level);
        item.setCaption(outlineItem.getTitle());
        try {
            PDDestination dest = outlineItem.getDestination();
            if (dest instanceof PDPageDestination) {
                PDPageDestination pageDest = (PDPageDestination) dest;
                item.setPageIndex(pageDest.retrievePageNumber());
            }
        } catch (Exception ignored) {
        }
        // 添加子节点
        Iterable<PDOutlineItem> outlineChildren = outlineItem.children();
        for (PDOutlineItem outlineChild : outlineChildren) {
            item.addSub(toOutlineItem(outlineChild, level + 1));
        }
        return item;
    }

    public BufferedImage renderImage(int pageIndex) {
        return renderImage(pageIndex, 1);
    }

    public BufferedImage renderImage(int pageIndex, float scale) {
        if (this.renderer == null) {
            this.renderer = new PDFRenderer(this.origin);
        }
        try {
            return this.renderer.renderImage(pageIndex, scale);
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
            throw new BusinessException(DocExceptionCodes.CAN_NOT_LOAD, FileExtensions.PDF.toUpperCase());
        }
    }

    public String getText() {
        try {
            return new PDFTextStripper().getText(this.origin);
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
        return null;
    }

    public void close() {
        try {
            this.origin.close();
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
    }

}
