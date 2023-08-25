package org.panda.tech.data.model.query.spec;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.data.model.query.FieldOrder;
import org.panda.tech.data.model.query.Pagination;
import org.panda.tech.data.model.query.QueryModel;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 分页查询条件。通过创建子类附带更多的查询条件
 */
public class Querying extends Pagination implements QueryModel, Paging {

    private static final long serialVersionUID = 7976726592026420060L;

    private QueryIgnoring ignoring;

    public Querying() {
    }

    public Querying(int pageSize, int pageNo) {
        super(pageSize, pageNo);
    }

    public Querying(int pageSize, int pageNo, List<FieldOrder> orders) {
        super(pageSize, pageNo, orders);
    }

    @Override
    public QueryIgnoring getIgnoring() {
        return this.ignoring;
    }

    public void setIgnoring(QueryIgnoring ignoring) {
        this.ignoring = ignoring;
        // 在忽略记录时，为了确保执行获取总数的动作，确保页大小>0
        if (ignoring == QueryIgnoring.RECORD && getPageSize() <= 0) {
            setPageSize(20);
        }
    }

    //////

    @Override
    public List<FieldOrder> getOrders() {
        return super.getOrders();
    }

    public void setOrderBy(String orderBy) {
        setOrders(null);
        if (StringUtils.isNotBlank(orderBy)) {
            String[] orders = decode(orderBy).split(Strings.COMMA);
            for (String order : orders) {
                FieldOrder fieldOrder = FieldOrder.of(order);
                if (fieldOrder != null) {
                    addOrder(fieldOrder);
                }
            }
        }
    }

    private String decode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

    public String getOrderBy() {
        return StringUtil.ifBlank(toOrderBy(getOrders()), null);
    }

    /**
     * 将指定查询排序序列转换排序语句，不含order by<br>
     * 如果无排序设置，则返回空字符串
     *
     * @param orders 查询排序序列
     * @return 排序语句
     */
    public static String toOrderBy(Collection<FieldOrder> orders) {
        StringBuilder orderBy = new StringBuilder();
        if (orders != null) {
            Set<String> fieldNames = new HashSet<>();
            for (FieldOrder order : orders) {
                String fieldName = order.getName();
                if (fieldName.contains("--")) { // 字段名不能包含--，这是sql的注释语法，可能造成sql注入漏洞
                    throw new IllegalArgumentException("Order field name cannot contain --");
                }
                if (fieldNames.add(fieldName)) { // 忽略重复的字段
                    orderBy.append(Strings.COMMA).append(order);
                } else { // 重复的字段输出警告日志
                    LogUtil.warn(Querying.class, "Repeated field({}) order is ignored.", fieldName);
                }
            }
            if (orderBy.length() > 0) {
                orderBy.deleteCharAt(0); // 去掉首位的多余逗号
            }
        }
        return orderBy.toString();
    }

    @SuppressWarnings("unchecked")
    public void decodeParamValue(String... ignoredParamNames) {
        ClassUtil.loopDynamicFields(getClass(), field -> {
            if (!ArrayUtils.contains(ignoredParamNames, field.getName())) {
                Class<?> fieldType = field.getType();
                if (fieldType == String.class) {
                    String value = (String) BeanUtil.getFieldValue(this, field);
                    if (StringUtils.isNotBlank(value)) {
                        String decodedValue = decode(value);
                        if (!value.equals(decodedValue)) {
                            BeanUtil.setFieldValue(this, field, decodedValue);
                        }
                    }
                } else if (fieldType.isArray() && fieldType.getComponentType() == String.class) {
                    String[] array = (String[]) BeanUtil.getFieldValue(this, field);
                    if (array != null) {
                        for (int i = 0; i < array.length; i++) {
                            String value = array[i];
                            array[i] = decode(value);
                        }
                    }
                } else if (Collection.class.isAssignableFrom(fieldType)) {
                    Collection<Object> collection = (Collection<Object>) BeanUtil.getFieldValue(this, field);
                    if (collection != null) {
                        if (collection instanceof List) {
                            List<Object> list = (List<Object>) collection;
                            for (int i = 0; i < list.size(); i++) {
                                Object obj = list.get(i);
                                if (obj instanceof String) {
                                    list.set(i, decode((String) obj));
                                }
                            }
                        } else {
                            List<Object> decodedList = new ArrayList<>();
                            Iterator<Object> iterator = collection.iterator();
                            while (iterator.hasNext()) {
                                Object obj = iterator.next();
                                if (obj instanceof String) {
                                    String decodedValue = decode((String) obj);
                                    if (!obj.equals(decodedValue)) {
                                        iterator.remove();
                                        decodedList.add(decodedValue);
                                    }
                                }
                            }
                            if (decodedList.size() > 0) {
                                try {
                                    collection.addAll(decodedList);
                                } catch (UnsupportedOperationException e) {
                                    collection = new HashSet<>(collection);
                                    collection.addAll(decodedList);
                                    BeanUtil.setFieldValue(this, field, collection);
                                }
                            }
                        }
                    }
                }
            }
            return true;
        });
    }

}
