package com.sy.keyword.common.client;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApiClientFactory {

    private final Map<String, ApiClient> apiClients;

    public ApiClientFactory(Map<String, ApiClient> apiClients) {
        this.apiClients = apiClients;
    }

    public ApiClient getClient(ApiCatalog apiCatalog){

        return apiClients.get(apiCatalog.getCode());
    }
}
