package org.panda.bamboo.common.util.algorithm;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.Assert;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 算法：校验和
 * 简单标识校验
 */
public class AlgoChecksum implements Algorithm {

    private final int length;
    private final int radix;

    public AlgoChecksum(int length, int radix) {
        Assert.isTrue(length > 0, "The length must be >0");
        this.length = length;
        Assert.isTrue(1 < radix && radix <= 36, () -> "The radix of value must be >1 and <=36");
        this.radix = radix;
    }

    public AlgoChecksum(int length) {
        this(length, 10);
    }

    public String visit(String s) {
        if (s == null) {
            return StringUtils.leftPad(Strings.EMPTY, this.length, '0');
        }
        int[] array;
        if (s.isEmpty()) {
            array = new int[]{toInt(Strings.EMPTY)};
        } else {
            array = new int[s.length()];
            for (int i = 0; i < s.length(); i++) {
                array[i] = toInt(s.substring(i, i + 1));
            }
        }
        long total = 0;
        for (int value : array) {
            total += value;
        }
        int checksum = (int) (total % ((int) Math.pow(this.radix, this.length)));
        return Integer.toString(checksum, this.radix);
    }

    private int toInt(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        int padding = Integer.BYTES - bytes.length;
        for (int i = 0; i < padding; i++) {
            buffer.put((byte) 0);
        }
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

}
