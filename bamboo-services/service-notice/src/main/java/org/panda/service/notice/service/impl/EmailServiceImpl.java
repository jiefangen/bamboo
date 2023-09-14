package org.panda.service.notice.service.impl;

import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.service.notice.common.NoticeConstants;
import org.panda.service.notice.core.NoticeSendSupport;
import org.panda.service.notice.core.domain.model.NoticeMode;
import org.panda.service.notice.core.domain.single.email.provider.EmailProvider;
import org.panda.service.notice.model.entity.NoticeSendRecord;
import org.panda.service.notice.model.param.CustomEmailParam;
import org.panda.service.notice.model.param.EmailParam;
import org.panda.service.notice.repository.NoticeSendRecordRepo;
import org.panda.service.notice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class EmailServiceImpl extends NoticeSendSupport implements EmailService {

    @Autowired
    private NoticeSendRecordRepo noticeSendRecordRepo;

    @Override
    protected NoticeMode getNoticeMode() {
        return NoticeMode.EMAIL;
    }

    @Override
    public String sendEmail(EmailParam emailParam) {
        List<String> addressees = emailParam.getAddressees();
        String[] noticeTargets = addressees.toArray(new String[0]);
        String type = emailParam.getEmailType();

        EmailProvider emailProvider = super.getEmailSender().getProvider(type);
        if (emailProvider == null) {
            return Commons.RESULT_FAILURE;
        }
        Map<String, Object> params = emailParam.getParams();
        Object sendResult = super.send(type, params, Locale.ROOT, noticeTargets);

        String title = emailProvider.getTitle(params, Locale.getDefault());
        String content = emailProvider.getContent(params, Locale.getDefault());
        String sendJson = JsonUtil.toJson(sendResult);
        this.saveSendRecord(sendJson, addressees.toString(), type, title, content);
        return sendJson;
    }

    @Override
    public String sendCustomEmail(CustomEmailParam emailParam) {
        List<String> addressees = emailParam.getAddressees();
        String[] noticeTargets = addressees.toArray(new String[0]);
        String title = emailParam.getTitle();
        String content = emailParam.getContent();
        Object sendResult = super.sendCustom(title, content, noticeTargets);

        String sendJson = JsonUtil.toJson(sendResult);
        this.saveSendRecord(sendJson, addressees.toString(), NoticeConstants.NOTICE_CUSTOM, title, content);
        return sendJson;
    }

    private void saveSendRecord(String sendResult, String addressees, String type, String title, String content) {
        NoticeSendRecord sendRecord = new NoticeSendRecord();
        NoticeMode noticeMode = getNoticeMode();
        sendRecord.setNoticeMode(EnumValueHelper.getValue(noticeMode));
        sendRecord.setType(type);
        sendRecord.setTitle(title);
        sendRecord.setContent(content);
        sendRecord.setNoticeTargets(addressees);
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
