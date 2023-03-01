package com.sy.keyword.common.client;

import com.sy.keyword.common.properties.KakaoApiConfig;
import com.sy.keyword.biz.search.vo.Place;
import com.sy.keyword.biz.search.vo.response.KakaoKeyword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Slf4j
@Service("kakao")
public class KakaoApiClient implements ApiClient{

    private final KakaoApiConfig kakaoApiConfig;

    KakaoApiClient(KakaoApiConfig kakaoApiConfig){
        this.kakaoApiConfig = kakaoApiConfig;
    }

    private final String tokenPrefix = "KakaoAK ";

    @Override
    public List<Place> getPlace(String keyword, CountDownLatch countDownLatch){
        WebClient webClient = WebClient.builder().baseUrl(kakaoApiConfig.getSearchurl())
                .defaultHeaders(
                        httpHeaders -> {
                            httpHeaders.add(HttpHeaders.CONTENT_TYPE,  MediaType.APPLICATION_JSON_VALUE);
                            httpHeaders.add(HttpHeaders.CONTENT_TYPE,  MediaType.APPLICATION_JSON_VALUE);
                            httpHeaders.add("Authorization", tokenPrefix+kakaoApiConfig.getToken());
                        }
                ).build();

        List<Place> kakaoResult = new ArrayList<>();

        webClient.get().uri(uriBuilder ->
                uriBuilder.queryParam("query", keyword)
                        .queryParam("size", 10)
                        .queryParam("page", 1)
                        .build())
                .retrieve()
                .bodyToMono(KakaoKeyword.class)
                .doAfterTerminate(() -> countDownLatch.countDown())
                .onErrorReturn(KakaoKeyword.builder().documents(new ArrayList<>()).build())
                .subscribe(e -> {
                    kakaoResult.addAll(e.getDocuments().stream().map(document ->
                            Place.builder()
                                    .placeName(document.getPlaceName())
                                    .address(document.getAddress())
                                    .roadAddress(document.getRoadAddress())
                                    .build())
                            .collect(Collectors.toList()));
                });

        return kakaoResult;
    }
}
