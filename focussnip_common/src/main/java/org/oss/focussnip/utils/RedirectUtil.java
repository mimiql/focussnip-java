package org.oss.focussnip.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedirectUtil {

    private static String prefix;

    public static String getRedirectUrl(String url){
        return prefix + url;
    }

    @Value("${server.servlet.context-path:}")
    public void setPrefix(String contextPath){
        RedirectUtil.prefix = contextPath;
    }
}
