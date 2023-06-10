package org.panda.tech.data.model.query;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;

import java.io.Serializable;
import java.util.Objects;

/**
 * 字段排序
 *
 * @author fangen
 */
public class FieldOrder implements Serializable, Cloneable {

    private static final long serialVersionUID = 7894501723760298654L;
    private static final String DESC = "desc";

    private String name;
    private boolean desc;

    public FieldOrder() {
    }

    public FieldOrder(String name, boolean desc) {
        this.name = name;
        this.desc = desc;
    }

    public static FieldOrder of(String order) {
        if (StringUtils.isNotBlank(order)) {
            order = order.trim();
            if (order.length() > 0) {
                String fieldName;
                boolean desc = false;
                int index = order.indexOf(Strings.SPACE);
                if (index > 0) {
                    fieldName = order.substring(0, index);
                    desc = FieldOrder.DESC.equalsIgnoreCase(order.substring(index + 1));
                } else {
                    fieldName = order;
                }
                return new FieldOrder(fieldName, desc);
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDesc() {
        return this.desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.desc);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FieldOrder other = (FieldOrder) obj;
        return Objects.equals(this.name, other.name) && this.desc == other.desc;
    }

    @Override
    public String toString() {
        if (this.desc) {
            return this.name + Strings.SPACE + DESC;
        }
        return this.name;
    }

    @Override
    public FieldOrder clone() {
        return new FieldOrder(this.name, this.desc);
    }

}
