package com.sy.keyword.common.client;

import com.sy.keyword.common.properties.NaverApiConfig;
import com.sy.keyword.biz.search.vo.Place;
import com.sy.keyword.biz.search.vo.response.NaverKeyword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Slf4j
@Service("naver")
public class NaverApiClient implements ApiClient{

    private final NaverApiConfig naverApiConfig;

    NaverApiClient(NaverApiConfig naverApiConfig){
        this.naverApiConfig = naverApiConfig;
    }


    //네이버는 최대가 5개
    @Override
    public List<Place> getPlace(String keyword, CountDownLatch countDownLatch) {

        WebClient webClient = WebClient.builder().baseUrl(naverApiConfig.getSearchurl())
                .defaultHeaders(
                        httpHeaders -> {
                            httpHeaders.add(HttpHeaders.CONTENT_TYPE,  MediaType.APPLICATION_JSON_VALUE);
                            httpHeaders.add(HttpHeaders.CONTENT_TYPE,  MediaType.APPLICATION_JSON_VALUE);
                            httpHeaders.add("X-Naver-Client-Id", naverApiConfig.getClientId());
                            httpHeaders.add("X-Naver-Client-Secret", naverApiConfig.getClientSecret());
                        }
                ).build();

        List<Place> naverResult = new ArrayList<>();

        webClient.get().uri(uriBuilder ->
                uriBuilder.queryParam("query", keyword)
                        .queryParam("display", 5)
                        .build())
                .retrieve()
                .bodyToMono(NaverKeyword.class)
                .doAfterTerminate(()-> countDownLatch.countDown())
                .subscribe(e -> {
                    e.getItems().stream().forEach(item -> log.info(item.getPlaceName()));
                    naverResult.addAll(e.getItems()
                            .stream()
                            .map(items -> Place.builder()
                                                    .placeName(items.getPlaceName())
                                                    .address(items.getAddress())
                                                    .roadAddress(items.getRoadAddress())
                                                    .build())
                            .collect(Collectors.toList()));
                });

        return naverResult;

    }
}
