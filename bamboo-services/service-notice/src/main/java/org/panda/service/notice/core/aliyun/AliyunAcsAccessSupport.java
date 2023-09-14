package org.panda.service.notice.core.aliyun;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.panda.bamboo.common.util.jackson.JsonUtil;

import java.util.Map;

/**
 * 阿里云ACS访问支持
 */
public abstract class AliyunAcsAccessSupport {

    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String VERSION = "2017-05-25";

    private IAcsClient client;

    private IAcsClient getClient() {
        if (this.client == null) {
            IClientProfile profile = DefaultProfile.getProfile(getRegionId(), getAccessKeyId(), getAccessKeySecret());
            this.client = new DefaultAcsClient(profile);
        }
        return this.client;
    }

    protected final Map<String, Object> post(String action, Map<String, Object> params) throws ClientException {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(DOMAIN);
        request.setSysVersion(VERSION);
        request.setSysAction(action);
        if (params != null) {
            params.forEach((name, value) -> {
                request.putQueryParameter(name, value.toString());
            });
        }
        CommonResponse response = getClient().getCommonResponse(request);
        return JsonUtil.json2Map(response.getData());
    }

    protected String getRegionId() {
        return "cn-hangzhou";
    }

    /**
     * @return 阿里云RAM访问控制的用户AccessKey ID
     */
    protected abstract String getAccessKeyId();

    /**
     * @return 阿里云RAM访问控制的用户AccessKey Secret
     */
    protected abstract String getAccessKeySecret();

}
