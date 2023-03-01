package com.sy.keyword.batch;

import com.sy.keyword.biz.search.service.HistoryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatisticsBatch {
    private final HistoryService historyService;

    public StatisticsBatch(HistoryService historyService){
        this.historyService = historyService;

    }

    @Scheduled(cron="0 0/10 * * * *")
    public void getStatistics(){
        historyService.giveStatistics();
    }
}
