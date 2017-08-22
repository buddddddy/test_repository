package org.mdlp.core.debug;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "debug", name = "dump-servlet-data")
public class DumpingFilterRegistrationBean extends FilterRegistrationBean {

    {
        setAsyncSupported(true);
        setOrder(Integer.MIN_VALUE);
        addUrlPatterns("/*");
        setFilter(new DumpingFilter());
        setName(getFilter().getClass().getName());
    }

}
