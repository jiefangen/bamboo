package org.panda.data.jpa.codegen;

/**
 * JPA实体列
 *
 * @author fangen
 */
public class JpaEntityColumn {

    private String name;
    private boolean exists = true;
    private Boolean autoIncrement;
    private Boolean nullable;
    private Integer length;
    private String definition;
    private Integer precision;
    private Integer scale;

    public JpaEntityColumn(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean isExists() {
        return this.exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public Boolean getAutoIncrement() {
        return this.autoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Boolean getNullable() {
        return this.nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public Integer getLength() {
        return this.length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getDefinition() {
        return this.definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Integer getPrecision() {
        return this.precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Integer getScale() {
        return this.scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }
}
