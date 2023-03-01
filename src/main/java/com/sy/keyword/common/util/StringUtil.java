package com.sy.keyword.common.util;

public class StringUtil {

    public static String replaceCity(String address){
        /*
        카카오 주소체계와 맞춤

        특별시 빼기
        광역시 뺴기

        경기도 -> 경기
        강원도 -> 강원
        경상남도 -> 경남
        충청남도 -> 충남
        전라남도 -> 전남
        경상북도 -> 경북
        충청북도 -> 충남
        전라북도 -> 전북
         */
        if(address.matches("^[가-힣]{2}특별시 .*")){
            address = address.replaceFirst("특별시", "");
        } else if(address.matches("^[가-힣]{2}광역시 .*")){
            address = address.replaceFirst("광역시", "");
        } else if(address.matches("^[가-힣]{2}도 .*")){
            address = address.replaceFirst("도", "");
        } else if(address.matches("^[가-힣]{2}남도 .*")){
            char startChar = address.charAt(0);
            address = startChar + "남 " + address.substring(5);
        } else if(address.matches("^[가-힣]{2}북도 .*")){
            char startChar = address.charAt(0);
            address = startChar + "북 " + address.substring(5);
        }
        return address;
    }
}
