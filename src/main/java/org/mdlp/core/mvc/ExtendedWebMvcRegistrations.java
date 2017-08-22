package org.mdlp.core.mvc;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class ExtendedWebMvcRegistrations extends WebMvcRegistrationsAdapter {

    @NotNull
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ExtendedRequestMappingHandlerMapping();
    }

}
