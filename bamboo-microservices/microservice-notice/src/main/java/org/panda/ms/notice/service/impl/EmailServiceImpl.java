package org.panda.ms.notice.service.impl;

import org.panda.ms.notice.common.NoticeConstants;
import org.panda.ms.notice.core.NoticeSendSupport;
import org.panda.ms.notice.core.domain.model.NoticeMode;
import org.panda.ms.notice.model.param.EmailParam;
import org.panda.ms.notice.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl extends NoticeSendSupport implements EmailService {

    @Override
    protected NoticeMode getNoticeMode() {
        return NoticeMode.EMAIL;
    }

    @Override
    public Object sendEmail(EmailParam emailParam) {
        List<String> addressees = emailParam.getAddressees();
        String[] noticeTargets = addressees.toArray(new String[0]);
        String type = emailParam.getEmailType();
        if (NoticeConstants.NOTICE_CUSTOM.equals(type)) { // 自定义发送内容
            return super.sendCustom(emailParam.getTitle(), emailParam.getContent(), noticeTargets);
        } else {
            return super.send(type, emailParam.getParams(), null, noticeTargets);
        }
    }
}
