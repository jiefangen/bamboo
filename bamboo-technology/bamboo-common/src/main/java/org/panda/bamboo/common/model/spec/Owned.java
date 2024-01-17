package org.panda.bamboo.common.model.spec;

import java.io.Serializable;

/**
 * 从属的
 *
 * @param <O> 所属者类型
 */
public interface Owned<O extends Serializable> {

    /**
     * @return 所属者
     */
    O getOwner();

}
