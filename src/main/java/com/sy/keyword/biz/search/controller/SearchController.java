package com.sy.keyword.biz.search.controller;

import com.sy.keyword.biz.search.service.HistoryService;
import com.sy.keyword.biz.search.service.SearchService;
import com.sy.keyword.biz.search.vo.KeywordResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchController {

    private final SearchService searchService;
    private final HistoryService historyService;

    public SearchController(HistoryService historyService, SearchService searchService){
        this.searchService = searchService;
        this.historyService = historyService;

    }

    @GetMapping("/search")
    public KeywordResult searchKeyword(@RequestParam String keyword){

        //키워드 검색 이력 H2 DB 적재
        historyService.saveKeywordSearchHistory(keyword);

        List<String> resultList = searchService.getKeyword(keyword);
        KeywordResult result = KeywordResult.builder().keyword(keyword).resultList(resultList).build();


        return result;

    }

}
