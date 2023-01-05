package org.panda.common.utils;

import java.util.Date;
import java.util.UUID;

/**
 * 生成UUID工具类
 *
 * @author fangen
 * @since JDK 11 2022/5/12
 */
public class UuidUtil {
    public static String[] chars = new String[]
            {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D",
            "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V","W", "X", "Y", "Z"};

    /**
     * 获取UUID标识码
     */
    public static String getUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 获取32位UUID标识码
     */
    public static String getUuid32(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").toLowerCase();
    }

    /**
     * 获取八位UUID标识码
     */
    public static String getShortUuid() {
        StringBuffer stringBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int strInteger = Integer.parseInt(str, 16);
            stringBuffer.append(chars[strInteger % 0x3E]);
        }
        return stringBuffer.toString();
    }

    /**
     * 获取8位不重复随机码
     * （取当前时间戳转化为十六进制）
     */
    public static String getUuidByTime(){
        long time = new Date().getTime();
        return Integer.toHexString((int)time);
    }
}
