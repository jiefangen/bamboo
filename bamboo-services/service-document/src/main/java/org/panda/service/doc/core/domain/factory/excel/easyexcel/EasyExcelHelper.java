package org.panda.service.doc.core.domain.factory.excel.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.panda.bamboo.common.util.LogUtil;
import org.slf4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * EasyExcel文档助手
 */
public class EasyExcelHelper {
	/**
	 * 日志工具
	 */
	private final Logger log = LogUtil.getLogger(getClass());

	/**
	 * 通用数据读取
	 *
	 * @param inputStream 文件流
	 * @return 读取的数据
	 */
	public Map<String, List<Map<Integer, String>>> read(InputStream inputStream) {
		// 读取所有数据容器
		Map<String, List<Map<Integer, String>>> contentMap = new LinkedHashMap<>();
		EasyExcel.read(inputStream, new AnalysisEventListener<Map<Integer, String>>() {
			/**
			 * 临时存储容器
			 */
			private List<Map<Integer, String>> cachedDataList = ListUtils.newArrayList();

			@Override
			public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
				// 解析表头数据，默认第一行
				cachedDataList.add(ConverterUtils.convertToStringMap(headMap, context));
			}
			@Override
			public void invoke(Map<Integer, String> data, AnalysisContext context) {
				// 单条数据解析
				cachedDataList.add(data);
			}
			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
				// 单个sheet数据解析完成
				String sheetName = context.readSheetHolder().getSheetName();
				contentMap.put(sheetName, cachedDataList);
				// 重置缓存临时容器
				cachedDataList = ListUtils.newArrayList();
			}
		}).doReadAll();
		return contentMap;
	}

	/**
	 * 通过指定数据模型读取
	 *
	 * @param inputStream 文件流
	 * @param dataClass 数据模型
	 * @return 读取的数据
	 */
	public <T> Map<String, List<T>> read(InputStream inputStream, Class<T> dataClass) {
		// 读取所有数据容器
		Map<String, List<T>> contentMap = new LinkedHashMap<>();
		EasyExcel.read(inputStream, dataClass, new AnalysisEventListener<T>() {
			/**
			 * 临时存储容器
			 */
			private List<T> cachedDataList = ListUtils.newArrayList();

			@Override
			public void invoke(T data, AnalysisContext context) {
				// 单条数据解析
				cachedDataList.add(data);
			}
			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
				// 单个sheet数据解析完成
				String sheetName = context.readSheetHolder().getSheetName();
				contentMap.put(sheetName, cachedDataList);
				// 重置缓存临时容器
				cachedDataList = ListUtils.newArrayList();
			}
		}).doReadAll();
		return contentMap;
	}

	/**
	 * 通过指定数据模型读取特定Sheet页
	 *
	 * @param inputStream 文件流
	 * @param sheetName Sheet名称
	 * @param dataClass 数据模型
	 * @return 读取数据结果
	 * @param <T> 泛型
	 */
	public <T> List<T> read(InputStream inputStream, String sheetName, Class<T> dataClass) {
		List<T> contentList = new LinkedList<>();
		EasyExcel.read(inputStream, dataClass, new ReadListener<T>() {
			/**
			 * 单次缓存的数据量
			 */
			public static final int BATCH_COUNT = 100;
			/**
			 * 临时存储容器
			 */
			private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

			@Override
			public void invoke(T data, AnalysisContext context) {
				cachedDataList.add(data);
				if (cachedDataList.size() >= BATCH_COUNT) {
					contentList.addAll(cachedDataList);
					// 存储完成清理缓存容器
					cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
				}
			}
			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
				// Sheet页数据解析完成后执行
				contentList.addAll(cachedDataList);
				log.info("{} sheet page parsing completed.", sheetName);
			}
		}).sheet(sheetName).doRead();
		return contentList;
	}

	public <T> void write(OutputStream outputStream, Map<String, List<T>> dataMap, Class<T> dataClass) {
		try (ExcelWriter excelWriter = EasyExcel.write(outputStream, dataClass).build()) {
			if (!dataMap.isEmpty()) {
				for (Map.Entry<String, List<T>> entry : dataMap.entrySet()) {
					String sheetName = entry.getKey();
					WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
					List<T> data = entry.getValue();
					excelWriter.write(data, writeSheet);
				}
				log.info("Excel data writing completed.");
			}
		}
	}
}
