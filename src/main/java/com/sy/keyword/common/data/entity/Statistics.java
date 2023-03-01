package com.sy.keyword.common.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Table(name="statistics")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@IdClass(StatisticsId.class)
public class Statistics{


    @Id
    @Column(name="keyword")
    private String keyword;

    @Id
    @Column(name="search_minute")
    private String statisticsMinute;

    @Column(name="search_day")
    private String searchDay;

    @Column(name="search_count")
    private int searchCount;


    @Column(name="create_date")
    @CreationTimestamp
    private java.sql.Timestamp createDate;

    public String getStatisticsMinute(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String statisticsTime = LocalDateTime.now().minusMinutes(9).format(formatter);
        statisticsMinute = statisticsTime.substring(8,11)+"0";
        return statisticsMinute;

    }

}
