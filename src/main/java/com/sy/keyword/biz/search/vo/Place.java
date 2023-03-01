package com.sy.keyword.biz.search.vo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Builder
@Data
public class Place {

    @NotBlank
    private String placeName;

    private String address;

    private String roadAddress;


}
