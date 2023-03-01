package com.sy.keyword.biz.search.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sy.keyword.common.util.StringUtil;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverKeyword {

    @JsonProperty("items")
    private List<items> items;

    @Getter
    public static class items {
        @JsonProperty("title")
        private String placeName;

        @JsonProperty("address")
        private String address;

        @JsonProperty("roadAddress")
        private String roadAddress;

        public String getAddress(){
            if(address != null && !"".equals(address)){
                address = StringUtil.replaceCity(address);
            }
            return address;
        }

        public String getPlaceName(){
            //태그 삭제
            //return placeName.replaceAll("<(/)?([a-zA-Z]*)>","");
            return placeName.replaceAll("<(/)?b>", "");
        }

        public String getRoadAddress(){
            if(roadAddress != null && !"".equals(roadAddress)){
                roadAddress = StringUtil.replaceCity(roadAddress);
            }
            return roadAddress;
        }



    }




}
