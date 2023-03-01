package com.sy.keyword.common.data.entity;

import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Table(name="keyword_history")
@Builder
@Entity
public class KeywordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int historyId;

    @NotEmpty
    private String keyword;

    @NotEmpty
    private String searchDay;


    @CreationTimestamp
    private java.sql.Timestamp createDate;


}
