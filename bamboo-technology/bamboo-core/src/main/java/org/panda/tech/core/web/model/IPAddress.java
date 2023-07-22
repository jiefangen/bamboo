package org.panda.tech.core.web.model;

import lombok.Data;

/**
 * IP归属地
 **/
@Data
public class IPAddress {
    /**
     * 国家
     */
    private String country;
    /**
     * 省/州
     */
    private String regionName;
    /**
     * 城市
     */
    private String city;
    /**
     * 运营商
     */
    private String isp;
    /**
     * 时区
     */
    private String timezone;
    /**
     * 国家code
     */
    private String countryCode;
    /**
     * 地区code
     */
    private String region;
}
