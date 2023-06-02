package org.panda.tech.core.spec.user;

/**
 * 具有用户特性的
 *
 * @param <I> 用户标识类型
 */
public interface UserSpecific<I extends UserIdentity<?>> {

    /**
     * 用户标识必须是整个系统全局唯一的，仅用于数据传递的，应该是可公开、不具备业务含义、人工无法识别的
     *
     * @return 用户标识
     */
    I getIdentity();

    /**
     * 用户名必须是在同一个用户类型中唯一的，可用于登录的，应该是不对外公开的
     *
     * @return 用户名
     */
    String getUsername();

    /**
     * 显示名称是可重复的，仅用于显示的，应该是可公开的，具有充分的业务含义，便于人工识别的
     *
     * @return 显示名称
     */
    String getCaption();

}
