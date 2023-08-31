package org.panda.ms.notice.service;

import org.panda.ms.notice.model.param.CustomSmsParam;
import org.panda.ms.notice.model.param.SmsParam;

/**
 * 短信服务
 *
 * @author fangen
 **/
public interface SmsService {

    Object sendSms(SmsParam smsParam);

    Object sendCustomSms(CustomSmsParam smsParam);

}
