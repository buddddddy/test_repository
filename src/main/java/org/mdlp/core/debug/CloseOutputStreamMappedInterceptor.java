package org.mdlp.core.debug;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.handler.MappedInterceptor;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Configuration
public class CloseOutputStreamMappedInterceptor {

    @NotNull
    @Bean
    @Order(LOWEST_PRECEDENCE)
    public MappedInterceptor closeOutputStreamInterceptor() {
        return new MappedInterceptor(new String[]{"/**"}, new CloseOutputStreamInterceptor());
    }

}
