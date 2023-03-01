package com.sy.keyword.biz.search.vo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ErrorResponse {
    private String errCode;
    private String errMsg;
}
