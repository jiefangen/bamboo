package org.panda.tech.data.model.entity.relation;

import java.io.Serializable;

public class SimpleRelationKey<L extends Serializable, R extends Serializable> extends AbstractRelationKey<L, R> {

    private final L left;
    private final R right;

    public SimpleRelationKey(L left, R rightId) {
        this.left = left;
        this.right = rightId;
    }

    public L getLeft() {
        return this.left;
    }

    public R getRight() {
        return this.right;
    }

}
