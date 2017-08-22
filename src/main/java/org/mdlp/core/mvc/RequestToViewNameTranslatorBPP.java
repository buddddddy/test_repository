package org.mdlp.core.mvc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;

@Component
public class RequestToViewNameTranslatorBPP implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DefaultRequestToViewNameTranslator) {
            DefaultRequestToViewNameTranslator translator = ((DefaultRequestToViewNameTranslator) bean);
            translator.setStripLeadingSlash(false);
        }

        return bean;
    }

}
