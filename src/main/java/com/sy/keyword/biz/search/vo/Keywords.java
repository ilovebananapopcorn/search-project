package com.sy.keyword.biz.search.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Keywords {
    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("searchCount")
    private int searchCount;

}
