package com.sy.keyword.common.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
public class StatisticsId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="keyword")
    private String keyword;

    @Column(name="statistics_minute")
    private String statisticsMinute;


}
