package org.mdlp.core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Locale;

@Component
@ConditionalOnClass(ObjectMapper.class)
public class JsonJackson2ViewResolver implements ViewResolver {

    @NotNull
    private final View view;

    @Autowired
    public JsonJackson2ViewResolver(ObjectMapper objectMapper) {
        this.view = new MappingJackson2JsonView(objectMapper);
    }

    @NotNull
    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        return view;
    }

}
