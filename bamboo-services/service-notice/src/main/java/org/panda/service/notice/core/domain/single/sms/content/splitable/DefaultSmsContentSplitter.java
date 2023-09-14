package org.panda.service.notice.core.domain.single.sms.content.splitable;

import org.panda.bamboo.common.util.lang.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 默认的短信内容分割器<br>
 * 数字、英文字母、英文标点可设置计为1个字符还是半个字符，其它字符均计为1个字符
 */
public class DefaultSmsContentSplitter implements SmsContentSplitter {

    private int size = 70;
    /**
     * 数字是否作为一个字符
     */
    private boolean numberAs1;
    /**
     * 字母是否作为一个字符
     */
    private boolean letterAs1;
    /**
     * 标点是否作为一个字符
     */
    private boolean punctuationAs1;

    /**
     * @param size 每条短信内容的最大长度
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @param numberAs1 数字是否计为1个字符
     */
    public void setNumberAs1(boolean numberAs1) {
        this.numberAs1 = numberAs1;
    }

    /**
     * @param letterAs1 字母是否计为1个字符
     */
    public void setLetterAs1(boolean letterAs1) {
        this.letterAs1 = letterAs1;
    }

    /**
     * @param punctuationAs1 标点是否计为1个字符
     */
    public void setPunctuationAs1(boolean punctuationAs1) {
        this.punctuationAs1 = punctuationAs1;
    }

    @Override
    public List<String> split(String content, int maxCount) {
        if (content.length() <= this.size) { // 总长度都不超过1条限制，直接返回
            return Collections.singletonList(content);
        }
        List<String> result = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        double length = 0;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            double clen = length(c);
            length += clen;
            if (length > this.size) { // 加上新字符长度后，长度已大于限制，则创建新的内容，重新开始计算长度
                result.add(sb.toString());
                if (result.size() >= maxCount) {
                    return result;
                }
                sb = new StringBuffer();
                length = clen;
            }
            sb.append(c);
        }
        if (sb.length() > 0 && result.size() < maxCount) { // 加上最后一段内容
            result.add(sb.toString());
        }
        return result;
    }

    private double length(char c) {
        if (StringUtil.isNumberChar(c) && !this.numberAs1) {
            return 0.5;
        }
        if (StringUtil.isLetterChar(c) && !this.letterAs1) {
            return 0.5;
        }
        if (StringUtil.isPunctuationChar(c) && !this.punctuationAs1) {
            return 0.5;
        }
        return 1;
    }

}
