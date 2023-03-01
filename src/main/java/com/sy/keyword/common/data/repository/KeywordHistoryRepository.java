package com.sy.keyword.common.data.repository;

import com.sy.keyword.common.data.entity.KeywordHistory;
import com.sy.keyword.common.data.entity.StatisticsInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordHistoryRepository extends JpaRepository<KeywordHistory, Integer> {

    @Query(value="SELECT count(*) as searchCount,"  +
                 "                       keyword, " +
                 "            search_day searchDay" +
                 "            from keyword_history " +
                "      where search_day = :searchDay " +
                " group by keyword, search_day order by searchCount",
            nativeQuery = true)
    List<StatisticsInterface> findCountBySearchDay(@Param("searchDay") String searchDay);


}
