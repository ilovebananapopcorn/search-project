package com.sy.keyword.biz.search.service;

import com.sy.keyword.common.client.ApiCatalog;
import com.sy.keyword.common.client.ApiClient;
import com.sy.keyword.common.client.ApiClientFactory;
import com.sy.keyword.common.exception.BizException;
import com.sy.keyword.biz.search.vo.Place;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiFunction;

@Slf4j
@Service
public class SearchService {
    private final ApiClientFactory apiClientFactory;

    public SearchService(ApiClientFactory apiClientFactory){
        this.apiClientFactory = apiClientFactory;
    }

    public List<String> getKeyword(String keyword){

        //검색 이력보고 최신 검색이 있으면 해당 결과 리턴

        CountDownLatch countDownLatch = new CountDownLatch(2);

        ApiClient kakoApiClient = apiClientFactory.getClient(ApiCatalog.KAKAO);
        List<Place> kakaoPlaceList = kakoApiClient.getPlace(keyword, countDownLatch);

        ApiClient naverApiClient = apiClientFactory.getClient(ApiCatalog.NAVER);
        List<Place> naverPlaceList = naverApiClient.getPlace(keyword, countDownLatch);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new BizException(e.getMessage());
        }

        List<String> result = mergeResultList(kakaoPlaceList, naverPlaceList);
        return result;

    }

    public List<String> mergeResultList(List<Place> kakaoList, List<Place> naverList){

        //maximum 10개로 (최대한) 만들기
        int naverCount = naverList.size();

        log.info("kakao Result : " + kakaoList.toString());
        log.info("naver Result : " + naverList.toString());

        while(kakaoList.size() + naverCount > 10){
            kakaoList.remove(kakaoList.size()-1);
        }

        List<String> resultList = new ArrayList<>();

        Queue<Place> removeQ = new LinkedList<>();

        for(Place kakao : kakaoList){
            for(Place naver : naverList){
                if(compareEqualityFunction.apply(kakao,naver)){
                    resultList.add(kakao.getPlaceName());
                    naverList.remove(naver);
                    removeQ.offer(kakao);
                    break;
                }
            }
        }

        while(removeQ.size() > 0){
            Place place = removeQ.poll();
            for(int index =0; index < kakaoList.size();index++){
                Place aPlace = kakaoList.get(index);
                if(!place.equals(aPlace)) {
                    resultList.add(aPlace.getPlaceName());
                } else {
                    for(int j = index; j >= 0; j--){
                        kakaoList.remove(j);
                    }
                    break;
                }
            }
        }

        for(Place kakao : kakaoList){
            resultList.add(kakao.getPlaceName());
        }

        for(Place naver : naverList){
            resultList.add(naver.getPlaceName());
        }

        log.info("mergePlacelist : " + resultList.toString());
        return resultList;
    }

    //placeName이 같은 상황에서, address나 roadAddress 둘 중 하나만 같은 경우에도 true를 리턴한다.
    private BiFunction<Place, Place, Boolean> compareEqualityFunction = (p1, p2) -> {
        if(p1.getPlaceName().replaceAll(" ","").equals(p2.getPlaceName().replaceAll(" ",""))){
            if(p1.getAddress() != null && p1.getAddress().equals(p2.getAddress())){
                //address가 같은 케이스
                return true;
            } else if(p1.getRoadAddress() != null && p1.getRoadAddress().equals(p2.getRoadAddress())){
                //roadaddress가 같은 케이스
                return true;
            } else if(p1.getAddress() != null && p2.getAddress() != null &&
                    (p1.getAddress().startsWith(p2.getAddress()) || p2.getAddress().startsWith(p1.getAddress()))){
               //층 수 등 세부정보가 다르게 입력된 경우 같은 경우로 처리하기 위해 starts.with 으로 마지막 주소 확인을 한다.
                return true;
            } else if(p1.getAddress() != null && p2.getAddress() != null &&
                    (p1.getAddress().startsWith(p2.getAddress()) || p2.getAddress().startsWith(p1.getAddress()))){
                //층 수 등 세부정보가 다르게 입력된 경우 같은 경우로 처리하기 위해 starts.with 으로 마지막 주소 확인을 한다.
                return true;
            } else if(p1.getRoadAddress() != null && p2.getRoadAddress() != null &&
                    (p1.getRoadAddress().startsWith(p2.getRoadAddress()) || p2.getRoadAddress().startsWith(p1.getRoadAddress()))){
                //층 수 등 세부정보가 다르게 입력된 경우 같은 경우로 처리하기 위해 starts.with 으로 마지막 주소 확인을 한다.
                return true;
            }
        }
        return false;
    };

}
