package com.github.forinil.hateoasduallayer.config;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import com.github.forinil.hateoasduallayer.entity.SearchResult;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import java.util.UUID;

@ClientCacheApplication(name = "HATEOASFrontendFacade")
@EnableGemfireRepositories(basePackages = "com.github.forinil.hateoasduallayer.repository")
public class GemFireConfig {

    @Bean("SearchCache")
    public ClientRegionFactoryBean<SearchResult, UUID> searchCacheRegion(GemFireCache gemfireCache) {

        ClientRegionFactoryBean<SearchResult, UUID> searchCacheRegion = new ClientRegionFactoryBean<>();

        searchCacheRegion.setCache(gemfireCache);
        searchCacheRegion.setClose(false);
        searchCacheRegion.setShortcut(ClientRegionShortcut.LOCAL);

        return searchCacheRegion;
    }
}
