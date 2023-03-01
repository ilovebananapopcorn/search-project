package com.sy.keyword.biz.search.controller;

import com.sy.keyword.biz.search.service.HistoryService;
import com.sy.keyword.biz.search.vo.RankResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankController {

    private final HistoryService historyService;

    public RankController(HistoryService historyService){
        this.historyService = historyService;

    }

    @GetMapping("/rank")
    public RankResult getRanking() throws InterruptedException {
        RankResult rankResult = historyService.getRanking();
        return rankResult;

    }

}
