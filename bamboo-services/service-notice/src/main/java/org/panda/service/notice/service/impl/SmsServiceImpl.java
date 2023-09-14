package org.panda.service.notice.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.service.notice.common.NoticeConstants;
import org.panda.service.notice.core.NoticeSendSupport;
import org.panda.service.notice.core.domain.model.NoticeMode;
import org.panda.service.notice.core.domain.single.sms.content.SmsContentProvider;
import org.panda.service.notice.model.entity.NoticeSendRecord;
import org.panda.service.notice.model.param.CustomSmsParam;
import org.panda.service.notice.model.param.SmsParam;
import org.panda.service.notice.repository.NoticeSendRecordRepo;
import org.panda.service.notice.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class SmsServiceImpl extends NoticeSendSupport implements SmsService {

    @Autowired
    private NoticeSendRecordRepo noticeSendRecordRepo;

    @Override
    protected NoticeMode getNoticeMode() {
        return NoticeMode.SMS;
    }

    @Override
    public String sendSms(SmsParam smsParam) {
        List<String> mobilePhones = smsParam.getMobilePhones();
        String type = smsParam.getSmsType();
        SmsContentProvider contentProvider = super.getSmsNotifier().getContentProvider(type);
        if (contentProvider == null || CollectionUtils.isEmpty(mobilePhones)) {
            return Commons.RESULT_FAILURE;
        }
        String[] noticeTargets = mobilePhones.toArray(new String[0]);
        Map<String, Object> params = smsParam.getParams();
        Object sendResult = super.send(type, params, Locale.ROOT, noticeTargets);

        String content = contentProvider.getContent(params, Locale.getDefault());
        String sendJson = JsonUtil.toJson(sendResult);
        this.saveSendRecord(sendJson, JsonUtil.toJson(mobilePhones), type,  content);
        return sendJson;
    }

    @Override
    public String sendCustomSms(CustomSmsParam smsParam) {
        List<String> mobilePhones = smsParam.getMobilePhones();
        String[] noticeTargets = mobilePhones.toArray(new String[0]);
        String content = smsParam.getContent();
        Object sendResult = super.sendCustom(Strings.STR_NULL, content, noticeTargets);

        String sendJson = JsonUtil.toJson(sendResult);
        this.saveSendRecord(sendJson, JsonUtil.toJson(mobilePhones), NoticeConstants.NOTICE_CUSTOM,
                content);
        return sendJson;
    }

    private void saveSendRecord(String sendResult, String mobilePhones, String type, String content) {
        NoticeSendRecord sendRecord = new NoticeSendRecord();
        NoticeMode noticeMode = getNoticeMode();
        sendRecord.setNoticeMode(EnumValueHelper.getValue(noticeMode));
        sendRecord.setType(type);
        sendRecord.setContent(content);
        sendRecord.setNoticeTargets(mobilePhones);
        String sendResultStr = String.valueOf(sendResult);
        if (Commons.RESULT_FAILURE.equals(sendResultStr)) {
            sendRecord.setNoticeStatus(0);
        } else {
            sendRecord.setNoticeStatus(1);
        }
        sendRecord.setNoticeResult(sendResultStr);
        sendRecord.setSendTime(LocalDateTime.now());
        noticeSendRecordRepo.save(sendRecord);
    }
}
