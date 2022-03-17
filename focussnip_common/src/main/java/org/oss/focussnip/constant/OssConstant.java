package org.oss.focussnip.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class OssConstant {
    @Value("${endpoint}")
    private String ENDPOINT;
    @Value("${accessKeyId}")
    private String ACCESSKEYID;
    @Value("${accessKeySecret}")
    private String ACCESSKEYSECRET;
    @Value("${bucketName}")
    private String BUCKETNAME;
    @Value("${filehost}")
    private String FILEHOST;
    @Value("${prefix}")
    private String PREFIX;

}
