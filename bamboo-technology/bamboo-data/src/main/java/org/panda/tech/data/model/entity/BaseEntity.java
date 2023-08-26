package org.panda.tech.data.model.entity;

import java.io.Serializable;

/**
 * 使用扩展仓库组件实体模型
 * 上层应用数据库实体模型必须继承才能使用仓库扩展功能
 *
 * @author fangen
 */
public class BaseEntity implements Entity, Serializable {

    private static final long serialVersionUID = -6814075272213275675L;

}
