package org.panda.tech.core.spec.user;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.MathUtil;

import java.util.Objects;

public class DefaultUserIdentity implements IntegerUserIdentity {

    private static final long serialVersionUID = -1645870192351832325L;

    private String type;
    private Integer id;

    public DefaultUserIdentity() {
    }

    public DefaultUserIdentity(String type, Integer id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultUserIdentity that = (DefaultUserIdentity) o;
        return Objects.equals(this.type, that.type) && Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.id);
    }

    @Override
    public String toString() {
        String s = this.id == null ? Strings.EMPTY : this.id.toString();
        if (this.type != null) {
            // 格式：id@type
            s += Strings.AT + this.type;
        }
        return s;
    }

    public static DefaultUserIdentity of(String s) {
        String type = null;
        Integer id;
        int index = s.indexOf(Strings.AT);
        if (index < 0) { // 只有id没有type
            id = MathUtil.parseInteger(s);
        } else { // 有type
            id = MathUtil.parseInteger(s.substring(0, index));
            type = s.substring(index + 1);
        }
        return new DefaultUserIdentity(type, id);
    }

}
