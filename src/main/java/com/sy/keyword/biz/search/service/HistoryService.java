package com.sy.keyword.biz.search.service;

import com.sy.keyword.common.data.entity.KeywordHistory;
import com.sy.keyword.common.data.entity.Statistics;
import com.sy.keyword.common.data.entity.StatisticsInterface;
import com.sy.keyword.common.data.repository.KeywordHistoryRepository;
import com.sy.keyword.common.data.repository.StatisticsRepository;
import com.sy.keyword.biz.search.vo.Keywords;
import com.sy.keyword.biz.search.vo.RankResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HistoryService {
    private final KeywordHistoryRepository keywordHistoryDao;
    private final StatisticsRepository statisticsDao;

    private final String datePattern = "yyyyMMdd";

    public HistoryService(KeywordHistoryRepository keywordHistoryDao, StatisticsRepository statisticsDao){
        this.keywordHistoryDao = keywordHistoryDao;
        this.statisticsDao = statisticsDao;
    }

    public void saveKeywordSearchHistory(String keyword){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        KeywordHistory keywordHistory = KeywordHistory.builder().keyword(keyword).searchDay(LocalDate.now().format(formatter)).build();
        keywordHistoryDao.save(keywordHistory);
    }

    @Transactional
    public void giveStatistics(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        String today = LocalDate.now().format(formatter);

        //당일 통계 데이터 삭제
        statisticsDao.deleteToday(today);
        
        //당일 통계 데이터 저장
        List<StatisticsInterface> resultList = keywordHistoryDao.findCountBySearchDay(today);

        if(resultList.size() > 0) {
            statisticsDao.saveAll(resultList.stream().map(si -> Statistics.builder()
                    .searchCount(si.getSearchCount())
                    .keyword(si.getKeyword())
                    .searchDay(si.getSearchDay())
                    .build()
            ).collect(Collectors.toList()));
        }
    }

    public RankResult getRanking(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        String today = LocalDate.now().format(formatter);
        List<Statistics> ranks = statisticsDao.findTop10BySearchDayOrderBySearchCountDesc(today);
        List<Keywords> keywords = ranks.stream().map(st -> Keywords.builder()
                        .searchCount(st.getSearchCount())
                        .keyword(st.getKeyword())
                        .build()).collect(Collectors.toList());
        return  RankResult.builder().keywords(keywords).build();

    }

}
