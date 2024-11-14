package org.panda.service.doc.core.domain.factory.excel.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * EasyExcel文档助手
 */
public class EasyExcelHelper {

	public List<ExcelBasicData> simpleRead(InputStream inputStream) {
		// 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
		// 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
		// 具体需要返回多少行可以在`PageReadListener`的构造函数设置
//		EasyExcel.read(inputStream, ExcelBasicData.class, new PageReadListener<ExcelBasicData>(dataList -> {
//			for (ExcelBasicData basicData : dataList) {
//				LogUtil.info(getClass(), "读取到一条数据{}", JsonUtil.toJson(basicData));
//			}
//		})).sheet().doRead();


		List<ExcelBasicData> dataList = new ArrayList<>();
		// 这里需要指定读用哪个class去读，然后读取第一个sheet文件流会自动关闭
		EasyExcel.read(inputStream, ExcelBasicData.class, new ReadListener<ExcelBasicData>() {
			/**
			 * 单次缓存的数据量
			 */
			public static final int BATCH_COUNT = 100;
			/**
			 * 临时存储容器
			 */
			private List<ExcelBasicData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

			@Override
			public void invoke(ExcelBasicData data, AnalysisContext context) {
				cachedDataList.add(data);
				if (cachedDataList.size() >= BATCH_COUNT) {
					saveData();
					// 存储完成清理list
					cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
				}
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
				saveData();
			}

			private void saveData() {
				dataList.addAll(cachedDataList);
			}
		}).sheet().doRead();
		return dataList;
	}
}
