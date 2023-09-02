package org.panda.tech.core.spec.terminal;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.ArrayUtil;

/**
 * 终端模型
 */
public class Terminal {
    /**
     * 程序类型
     */
    private Program program;

    /**
     * 设备类型
     */
    private Device device;

    /**
     * 操作系统
     */
    private OS os;

    public Terminal() {
    }

    public Terminal(Program program, Device device, OS os) {
        this.program = program;
        this.device = device;
        this.os = os;
    }

    public static Terminal of(String s) {
        Program program = null;
        Device device = null;
        OS os = null;
        // 允许空的终端表示
        if (StringUtils.isNotBlank(s)) {
            String[] array = s.split(Strings.MINUS);
            program = EnumUtils.getEnum(Program.class, ArrayUtil.get(array, 0));
            device = EnumUtils.getEnum(Device.class, ArrayUtil.get(array, 1));
            os = EnumUtils.getEnum(OS.class, ArrayUtil.get(array, 2));
        }
        return new Terminal(program, device, os);
    }

    /**
     * @return 程序类型
     */
    public Program getProgram() {
        return this.program;
    }

    /**
     * @param program 程序类型
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    /**
     * @return 设备类型
     */
    public Device getDevice() {
        return this.device;
    }

    /**
     * @param device 设备类型
     */
    public void setDevice(Device device) {
        this.device = device;
    }

    /**
     * @return 操作系统
     */
    public OS getOs() {
        return this.os;
    }

    /**
     * @param os 操作系统
     */
    public void setOs(OS os) {
        this.os = os;
    }

    public boolean supports(Terminal terminal) {
        // 程序类型属性为null，视为支持所有程序类型
        if (this.program != null && this.program != terminal.program) {
            return false;
        }
        // 设备属性为null，视为支持所有设备
        if (this.device != null && this.device != terminal.device) {
            return false;
        }
        // 操作系统属性为null，视为支持所有操作系统
        if (this.os != null && this.os != terminal.os) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.program != null) {
            sb.append(this.program.name());
        }
        sb.append(Strings.MINUS);
        if (this.device != null) {
            sb.append(this.device.name());
        }
        sb.append(Strings.MINUS);
        if (this.os != null) {
            sb.append(this.os.name());
        }
        return sb.toString();
    }

}
