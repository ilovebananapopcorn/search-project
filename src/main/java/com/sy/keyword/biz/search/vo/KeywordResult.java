package com.sy.keyword.biz.search.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class KeywordResult {
    String keyword;
    LocalDateTime timestamp;
    List<String> resultList;

    public LocalDateTime getTimestamp(){
        return LocalDateTime.now();
    }

}
