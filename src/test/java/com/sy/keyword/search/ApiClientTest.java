package com.sy.keyword.search;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.keyword.biz.search.service.SearchService;
import com.sy.keyword.biz.search.vo.Place;
import com.sy.keyword.biz.search.vo.response.KakaoKeyword;
import com.sy.keyword.biz.search.vo.response.NaverKeyword;
import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setPrintAssertionsDescription;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ApiClientTest {
    @Autowired
    private SearchService searchService;


    @Test
    void apiClientTest() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //테스트 케이스 1 : 값 1개 일치, 카카오: 5개 리턴 네이버 : 5개 리턴, 총합 9개
        String kakaoResponse = Files.readString(Paths.get("src/test/resources/testdata/kakao_keyword1.json"));
        KakaoKeyword kakaoKeyword = mapper.readValue(kakaoResponse, KakaoKeyword.class);
        List<Place> kakaoResult = getKakaoResult.apply(kakaoKeyword);
        String naverResponse = Files.readString(Paths.get("src/test/resources/testdata/naver_keyword1.json"));
        NaverKeyword naverKeyword = mapper.readValue(naverResponse, NaverKeyword.class);
        List<Place> naverResult = getNaverResult.apply(naverKeyword);
        List<String> resultList = searchService.mergeResultList(kakaoResult, naverResult);
        List<String> expectList = Arrays.asList("카카오뱅크"
                , "카카오뱅크 고객센터"
                , "제니엘 카카오뱅크고객센터"
                ,"현대오일뱅크 카카오주유소"
                ,"이마트24 셀프카카오뱅크서울역점"
                ,"신한은행 카카오출장소"
                ,"신한 카카오 ATM 점두365"
                ,"이마트24 self카카오뱅크서울역점"
                ,"이마트24 self카카오뱅크판교점");
        Assertions.assertLinesMatch(resultList, expectList);

        //테스트케이스2 : 값 2개 일치, 카카오: 4개 리턴 네이버 : 5개 리턴
        kakaoResponse = Files.readString(Paths.get("src/test/resources/testdata/kakao_keyword2.json"));
        kakaoKeyword = mapper.readValue(kakaoResponse, KakaoKeyword.class);
        kakaoResult = getKakaoResult.apply(kakaoKeyword);
        naverResponse = Files.readString(Paths.get("src/test/resources/testdata/naver_keyword2.json"));
        naverKeyword = mapper.readValue(naverResponse, NaverKeyword.class);
        naverResult = getNaverResult.apply(naverKeyword);
        resultList = searchService.mergeResultList(kakaoResult, naverResult);
        expectList = Arrays.asList("오마이데이터"
                , "마이데이터지원센터"
                , "마이데이터랩"
                , "마이데이터랩"
                ,"마이로직"
                ,"마이셀럽스"
                ,"코드에프");
        Assertions.assertLinesMatch(resultList, expectList);

        //테스트케이스3: 네이버 리턴값없음, 카카오 10개
        kakaoResponse = Files.readString(Paths.get("src/test/resources/testdata/kakao_keyword3.json"));
        kakaoKeyword = mapper.readValue(kakaoResponse, KakaoKeyword.class);
        kakaoResult = getKakaoResult.apply(kakaoKeyword);
        naverResponse = Files.readString(Paths.get("src/test/resources/testdata/naver_keyword3.json"));
        naverKeyword = mapper.readValue(naverResponse, NaverKeyword.class);
        naverResult = getNaverResult.apply(naverKeyword);
        resultList = searchService.mergeResultList(kakaoResult, naverResult);
        expectList = Arrays.asList("하히후헤호1"
                , "하히후헤호2"
                , "하히후헤호3"
                , "하히후헤호4"
                ,"하히후헤호5"
                ,"하히후헤호6"
                ,"하히후헤호7"
                ,"하히후헤호8"
                ,"하히후헤호9"
                ,"하히후헤호10");
        Assertions.assertLinesMatch(resultList, expectList);

    }

    private Function<KakaoKeyword, List<Place>> getKakaoResult = (keyword) -> {
        return keyword.getDocuments().stream().map(document ->
                Place.builder()
                        .placeName(document.getPlaceName())
                        .address(document.getAddress())
                        .roadAddress(document.getRoadAddress())
                        .build())
                .collect(Collectors.toList());
    };

    private Function<NaverKeyword, List<Place>> getNaverResult = (keyword) -> {
        return keyword.getItems().stream().map(items ->
                Place.builder()
                        .placeName(items.getPlaceName())
                        .address(items.getAddress())
                        .roadAddress(items.getRoadAddress())
                        .build())
                .collect(Collectors.toList());
    };


}
