package com.sy.keyword.common.client;

import com.sy.keyword.biz.search.vo.Place;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public interface ApiClient {

    List<Place> getPlace(String keyword, CountDownLatch countDownLatch);
}
