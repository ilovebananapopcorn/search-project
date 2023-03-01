package com.sy.keyword.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix="kakao")
public class KakaoApiConfig {

    String searchurl;

    String token;
}
