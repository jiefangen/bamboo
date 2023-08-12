package org.panda.tech.data.datasource;

/**
 * 数据源上下文处理器
 *
 * @author fangen
 **/
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<>();

    public static void setDataSourceKey(String key) {
        dataSourceHolder.set(key);
    }

    public static String getDataSourceKey() {
        return dataSourceHolder.get();
    }

    public static void clearDataSourceKey() {
        dataSourceHolder.remove();
    }
}
