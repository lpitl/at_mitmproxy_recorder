package ru.bcs.requestrecorder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bcs.at.library.core.core.helpers.MitmInterceptedMessage;
import ru.bcs.requestrecorder.MessageCache;

@Configuration
public class MessageCacheConfiguration {

    @Value("${cache.expireSec}")
    private int expireSec;

    @Value("${cache.cleanupPeriodSec}")
    private int cleanupPeriodSec;

    @Value("${cache.maxSize}")
    private int maxSize;

    @Bean
    MessageCache<String, MitmInterceptedMessage> messageCacheInitialize() {
        return new MessageCache<>(expireSec, cleanupPeriodSec, maxSize);
    }

}
