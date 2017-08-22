package org.mdlp.core.ws;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointExceptionResolver;

@Component
public class LoggingEndpointExceptionResolver implements EndpointExceptionResolver, PriorityOrdered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingEndpointExceptionResolver.class);

    @Override
    public boolean resolveException(MessageContext messageContext, Object endpoint, @NotNull Exception ex) {
        logger.error(ex.getMessage(), ex);
        return false;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
