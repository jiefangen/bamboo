package org.panda.service.doc.core.domain.factory.word.helper;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.constant.file.FileExtensions;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.io.IOUtil;
import org.panda.service.doc.common.DocExceptionCodes;
import org.panda.service.doc.common.util.DocUtil;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Word 2003及以下文档，扩展名为doc
 */
public class WordDocHelper {

	private HWPFDocument origin;

	public WordDocHelper(InputStream in) {
		try {
			this.origin = new HWPFDocument(in);
		} catch (IOException e) {
			throw new BusinessException(DocUtil.getError(DocExceptionCodes.CAN_NOT_LOAD), FileExtensions.DOC.toUpperCase());
		}
	}

	public HWPFDocument getOrigin() {
		return this.origin;
	}

	public String convertToHtml(String encoding) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		convertToHtml(out);
		return out.toString(encoding);
	}

	public void convertToHtml(OutputStream out) {
		try {
			WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
					DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

			wordToHtmlConverter.setPicturesManager((content, pictureType, suggestedName, widthInches, heightInches) -> {
				return IOUtil.toBase64Data(content, pictureType.getExtension());
			});

			wordToHtmlConverter.processDocument(this.origin);
			Document htmlDocument = wordToHtmlConverter.getDocument();
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(out);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer serializer = factory.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, Strings.ENCODING_UTF8);
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, FileExtensions.HTML);
			serializer.transform(domSource, streamResult);
		} catch (Exception e) {
			LogUtil.error(getClass(), e);
		}
	}

	public String getText() {
		WordExtractor extractor = new WordExtractor(this.origin);
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
