package com.publicapi.test.domain.map.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "openapi")
public class OpenApi {
    public static final String BASE_URL="http://openapi.seoul.go.kr:8088";
    private String key;
    public static final String KEY_PARAM_NAME = "KEY";
    public static final String TYPE_PARAM_NAME = "TYPE";
    public static final String SERVICE_PARAM_NAME = "SERVICE";
    public static final String START_INDEX_PARAM_NAME = "START_INDEX";
    public static final String END_INDEX_PARAM_NAME = "END_INDEX";


}
