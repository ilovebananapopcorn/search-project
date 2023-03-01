package com.sy.keyword.biz.search.vo.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class KeywordSearch {
    String keyword;
}
