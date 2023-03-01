package com.sy.keyword.common.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApiCatalog{
    KAKAO("kakao", "카카오"),
    NAVER("naver", "네이버")
    ;
    
    private String code;
    private String desc;
}
