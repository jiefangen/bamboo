package org.panda.tech.core.spec;

/**
 * 命名支持
 */
public class NamedSupport implements Named {

    private String name;

    protected NamedSupport(String name) {
        this.name = name;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    protected void setName(String name) {
        this.name = name;
    }

}
