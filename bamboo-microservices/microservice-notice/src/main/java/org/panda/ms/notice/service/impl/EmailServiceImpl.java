package org.panda.ms.notice.service.impl;

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
        return super.send(emailParam.getEmailType(), emailParam.getParams(), emailParam.getLocale(), noticeTargets);
    }

}
