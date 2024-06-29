package org.panda.support.openapi.model.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信模板消息
 *
 * @author fangen
 */
public class WechatTemplateMessage {

    private WechatMessageText first;
    private List<WechatMessageText> keywords;
    private WechatMessageText remark;

    public WechatMessageText getFirst() {
        return this.first;
    }

    public void setFirst(WechatMessageText first) {
        this.first = first;
    }

    public List<WechatMessageText> getKeywords() {
        return this.keywords;
    }

    public void setKeywords(List<WechatMessageText> keywords) {
        this.keywords = keywords;
    }

    public WechatMessageText getRemark() {
        return this.remark;
    }

    public void setRemark(WechatMessageText remark) {
        this.remark = remark;
    }

    public Map<String, WechatMessageText> toMap() {
        Map<String, WechatMessageText> map = new HashMap<>();
        map.put("first", this.first);
        if (this.keywords != null) {
            for (int i = 0; i < this.keywords.size(); i++) {
                map.put("keyword" + (i + 1), this.keywords.get(i));
            }
        }
        map.put("remark", this.remark);
        return map;
    }

}
