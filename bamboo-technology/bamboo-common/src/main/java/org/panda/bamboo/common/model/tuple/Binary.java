package org.panda.bamboo.common.model.tuple;

import java.util.Objects;

/**
 * 二元体
 *
 * @param <L> 左元类型
 * @param <R> 右元类型
 * @see Binate
 */
public class Binary<L, R> implements Binate<L, R>, Cloneable {
    private L left;
    private R right;

    public Binary(L left, R right) {
        this.left = left;
        this.right = right;
    }

    protected void setLeft(L left) {
        this.left = left;
    }

    @Override
    public L getLeft() {
        return this.left;
    }

    protected void setRight(R right) {
        this.right = right;
    }

    @Override
    public R getRight() {
        return this.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.left, this.right);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Binate)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Binate<L, R> other = (Binate<L, R>) obj;
        return Objects.equals(this.left, other.getLeft())
                && Objects.equals(this.right, other.getRight());
    }

    @Override
    public Binary<L, R> clone() {
        return new Binary<>(this.left, this.right);
    }

    @Override
    public String toString() {
        return "(" + this.left + "," + this.right + ")";
    }
}
