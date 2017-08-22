package org.mdlp.basestate.data.processor;

import org.mdlp.data.log.LogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Component
public class RestUtil {
    private RestTemplate template;

    @Autowired
    public RestUtil(RestTemplateBuilder restTemplateBuilder) {
        restTemplateBuilder = restTemplateBuilder.setConnectTimeout(600000).
                setReadTimeout(600000).
                requestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        this.template = restTemplateBuilder.build();

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new LoggingRequestInterceptor());
        template.setInterceptors(interceptors);

        List<HttpMessageConverter<?>> converters = template.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                try {
                    List<MediaType> mediaTypes = new ArrayList<>();
                    mediaTypes.addAll(converter.getSupportedMediaTypes());
                    mediaTypes.add(MediaType.TEXT_HTML);
                    ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(mediaTypes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public RestTemplate getTemplate() {
        return template;
    }
}
