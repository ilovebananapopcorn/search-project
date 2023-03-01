package com.sy.keyword.biz.search.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoKeyword {

    @JsonProperty("documents")
    private List<documents> documents;

    @Getter
    public static class documents {
        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("address_name")
        private String address;

        @JsonProperty("road_address_name")
        private String roadAddress;
    }


}
