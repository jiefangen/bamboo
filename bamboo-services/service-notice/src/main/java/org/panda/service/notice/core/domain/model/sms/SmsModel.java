package org.panda.service.notice.core.domain.model.sms;

import org.panda.bamboo.common.model.DomainModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 短信模型
 */
public class SmsModel implements Sms, DomainModel {
    /**
     * 内容清单
     */
    private List<String> contents;
    /**
     * 手机号码清单
     */
    private String[] cellphones;
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
    /**
     * 接收时间
     */
    private LocalDateTime receiveTime;

    @Override
    public List<String> getContents() {
        if (this.contents == null) {
            this.contents = new ArrayList<>();
        }
        return this.contents;
    }

    /**
     * @param contents 内容清单
     */
    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    @Override
    public String[] getCellphones() {
        if (this.cellphones == null) {
            this.cellphones = new String[0];
        }
        return this.cellphones;
    }

    /**
     * @param cellphones 手机号码清单
     */
    public void setCellphones(String... cellphones) {
        this.cellphones = cellphones;
    }

    @Override
    public LocalDateTime getSendTime() {
        return this.sendTime;
    }

    /**
     * @param sendTime 发送时间
     */
    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public LocalDateTime getReceiveTime() {
        return this.receiveTime;
    }

    /**
     * @param receiveTime 接收时间
     */
    public void setReceiveTime(LocalDateTime receiveTime) {
        this.receiveTime = receiveTime;
    }
}
