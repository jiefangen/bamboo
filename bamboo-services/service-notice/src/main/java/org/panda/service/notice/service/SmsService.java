package org.panda.service.notice.service;

import org.panda.service.notice.model.param.CustomSmsParam;
import org.panda.service.notice.model.param.SmsParam;

/**
 * 短信服务
 *
 * @author fangen
 **/
public interface SmsService {

    String sendSms(SmsParam smsParam);

    String sendCustomSms(CustomSmsParam smsParam);

}
