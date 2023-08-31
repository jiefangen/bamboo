package org.panda.ms.notice.service;

import org.panda.ms.notice.model.param.CustomEmailParam;
import org.panda.ms.notice.model.param.EmailParam;

/**
 * 邮件服务
 *
 * @author fangen
 **/
public interface EmailService {

    Object sendEmail(EmailParam emailParam);

    Object sendCustomEmail(CustomEmailParam emailParam);

}
