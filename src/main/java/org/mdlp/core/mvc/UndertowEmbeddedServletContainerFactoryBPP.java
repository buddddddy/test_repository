package org.mdlp.core.mvc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.stereotype.Component;

@Component
public class UndertowEmbeddedServletContainerFactoryBPP implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof UndertowEmbeddedServletContainerFactory) {
            UndertowEmbeddedServletContainerFactory factory = ((UndertowEmbeddedServletContainerFactory) bean);
            //factory.addDeploymentInfoCustomizers((UndertowDeploymentInfoCustomizer) deploymentInfo -> deploymentInfo.setDefaultEncoding("UTF-8"));
        }

        return bean;
    }

}
