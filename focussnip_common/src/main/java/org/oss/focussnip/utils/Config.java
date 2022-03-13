package org.oss.focussnip.utils;

/**
 * 系统参数配置类，配置了系统用到参数
 *
 */
public interface Config {


    /**
     * 系统语言环境，默认为中文zh
     */
    String LANGUAGE = "zh";

    /**
     * 系统国家环境，默认为中国CN
     */
    String COUNTRY = "CN";

    /**
     * servletContext
     */
    String SERVLETCONTEXT_KEY = "servletContext";

    /**
     * 提示信息配置文件名
     */
    String MESSAGE = "i18n/messages";

    /**
     * jwt签发者的名称
     */
    String JWT_ISSUER = "focussnip";

    /**
     * jwt自定义属性名称
     */
    String JWT_CLIENT_USERNAME = "username";
    String JWT_APPLICATION_KEY = "focussnip";
    /**
     * 客户端传token参数名称
     */
    String JWT_CUSTOMER_TOKEN_NAME = "Authorization";
    Long JWT_ACTIVE_TIME = 60*60*24*7*1000L;
}
