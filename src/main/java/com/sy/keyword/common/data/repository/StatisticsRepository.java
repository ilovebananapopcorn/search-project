package com.sy.keyword.common.data.repository;


import com.sy.keyword.common.data.entity.Statistics;
import com.sy.keyword.common.data.entity.StatisticsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, StatisticsId> {

    @Modifying
    @Query(value = "DELETE FROM statistics WHERE search_day = :searchDay", nativeQuery = true)
    void deleteToday(@Param("searchDay") String searchDay);

    List<Statistics> findTop10BySearchDayOrderBySearchCountDesc(@Param("search_day") String searchDay);

}
