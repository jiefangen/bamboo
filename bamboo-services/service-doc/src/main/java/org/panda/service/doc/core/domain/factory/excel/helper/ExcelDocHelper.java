package org.panda.service.doc.core.domain.factory.excel.helper;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.panda.bamboo.common.constant.file.FileExtensions;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.service.doc.common.DocExceptionCodes;
import org.panda.service.doc.common.util.DocUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * Excel文档助手
 */
public class ExcelDocHelper {

	private HSSFWorkbook origin;

	public ExcelDocHelper(InputStream in) {
		try {
			this.origin = new HSSFWorkbook(in);
		} catch (IOException e) {
			throw new BusinessException(DocUtil.getError(DocExceptionCodes.CAN_NOT_LOAD), FileExtensions.XLS);
		}
	}

	public HSSFWorkbook getOrigin() {
		return this.origin;
	}

	public String getText() {
		ExcelExtractor extractor = new ExcelExtractor(this.origin);
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
