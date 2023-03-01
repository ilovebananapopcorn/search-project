package com.sy.keyword.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix="naver")
public class NaverApiConfig {

    String searchurl;

    String clientId;

    String clientSecret;
}
