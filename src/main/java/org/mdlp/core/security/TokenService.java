package org.mdlp.core.security;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.mdlp.core.cache.CachingConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;

import java.util.UUID;

/**
 * Created by ssuvorov on 03.04.2017.
 */
public class TokenService {
    private static final Cache cache = CacheManager.getInstance().getCache(CachingConfig.SESSION_CACHE);
    public static final int HALF_AN_HOUR_IN_MILLISECONDS = 30 * 60 * 1000;

    @Scheduled(fixedRate = HALF_AN_HOUR_IN_MILLISECONDS)
    public void evictExpiredTokens() {
        cache.evictExpiredElements();
    }

    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    public void store(String token, Authentication authentication) {
        cache.put(new Element(token, authentication));
    }

    public boolean contains(String token) {
        return cache.get(token) != null;
    }

    public Authentication retrieve(String token) {
        return (Authentication) cache.get(token).getObjectValue();
    }
}
