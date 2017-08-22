package org.mdlp.core.ws;

import org.springframework.boot.context.embedded.AbstractConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.stereotype.Component;

import static org.springframework.http.MediaType.TEXT_XML_VALUE;

@Component
public class WebServiceCustomizer implements EmbeddedServletContainerCustomizer {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        final MimeMappings mappings;
        if (container instanceof AbstractConfigurableEmbeddedServletContainer) {
            mappings = ((AbstractConfigurableEmbeddedServletContainer) container).getMimeMappings();
        } else {
            mappings = new MimeMappings(MimeMappings.DEFAULT);
        }

        mappings.add("wsdl", TEXT_XML_VALUE);
        container.setMimeMappings(mappings);
    }

}
