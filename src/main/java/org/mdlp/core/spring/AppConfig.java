package org.mdlp.core.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by SSuvorov on 06.04.2017.
 */
@Configuration
public class AppConfig {
    @Bean
    public ThreadPoolTaskExecutor getTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(15);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}
