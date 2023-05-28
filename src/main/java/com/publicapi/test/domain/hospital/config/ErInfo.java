package com.publicapi.test.domain.hospital.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "erinfo")
public class ErInfo {

    public static final String BASE_URL="http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEmrrmRltmUsefulSckbdInfoInqire";
    public static final String KEY_PARAM_NAME = "serviceKey";

    private String key;

    //주소(시도)
    public static final String SI_PARAM_NAME = "STAGE1";
    
    //주소(시군구)
    public static final String GU_PARAM_NAME = "STAGE2";
    public static final String PAGE_PARAM_NAME = "pageNo";
    public static final String ROW_PARAM_NAME = "numOfRows";

}
