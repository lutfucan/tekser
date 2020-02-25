package com.tekser.configuration;


import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "cache.allUsersPageable", "cache.allUsers",
                "cache.allUsersEagerly", "cache.userByEmail", "cache.userById", "cache.allRoles",
                "cache.roleByName", "cache.roleById", "cache.byNameContaining", "cache.bySurnameContaining",
                "cache.byUsernameContaining", "cache.byEmailContaining", "cache.allClientsPageable",
                "cache.allClients", "cache.clientByEmail", "cache.clientById", "cache.allClientsEagerly",
                "cache.byNameContaining", "cache.bySurnameContaining", "cache.byEmailContaining",
                "cache.byPhoneContaining", "cache.allReceiptsPageable", "cache.allReceipts",
                "cache.receiptByClientEmail", "cache.reciptByClientId", "cache.allReceiptsEagerly",
                "cache.bySerialNumberContaining", "cache.byId", "cache.receiptById");
    }
}
