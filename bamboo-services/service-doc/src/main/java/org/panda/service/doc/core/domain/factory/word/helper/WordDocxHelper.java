package org.panda.service.doc.core.domain.factory.word.helper;

import fr.opensagres.poi.xwpf.converter.xhtml.Base64EmbedImgManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.constant.file.FileExtensions;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.service.doc.common.DocExceptionCodes;
import org.panda.service.doc.common.util.DocUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Word 2007及以上文档，扩展名为docx
 */
public class WordDocxHelper {

	private XWPFDocument origin;

	public WordDocxHelper(InputStream in) {
		try {
			this.origin = new XWPFDocument(in);
		} catch (IOException e) {
			throw new BusinessException(DocUtil.getError(DocExceptionCodes.CAN_NOT_LOAD), FileExtensions.DOCX.toUpperCase());
		}
	}

	public XWPFDocument getOrigin() {
		return this.origin;
	}

	public String convertToHtml(String encoding) throws IOException {
		XHTMLOptions options = XHTMLOptions.create().indent(4).setImageManager(new Base64EmbedImgManager());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XHTMLConverter.getInstance().convert(this.origin, out, options);
		String html = out.toString(encoding);
		html = html.replaceAll("white-space:pre-wrap;", Strings.EMPTY);
		html = html.replaceAll("\\n\\s*</span>", "</span>");
		html = html.replaceAll("</span>\\n\\s*<span ", "</span><span ");
		html = html.replaceAll("</span>\\n\\s*<span>", "</span><span>");
		return html;
	}

	public void convertToHtml(OutputStream out) {
		try {
			String html = convertToHtml(Strings.ENCODING_UTF8);
			IOUtils.write(html, out, StandardCharsets.UTF_8);
		} catch (IOException e) {
			LogUtil.error(getClass(), e);
		}
	}

	public String getText() {
		XWPFWordExtractor extractor = new XWPFWordExtractor(this.origin);
		String text = extractor.getText();
		try {
			extractor.close();
		} catch (IOException e) {
			LogUtil.error(getClass(), e);
		}
		return text;
	}

	public void close() {
		try {
			this.origin.close();
		} catch (IOException e) {
			LogUtil.error(getClass(), e);
		}
	}

}
