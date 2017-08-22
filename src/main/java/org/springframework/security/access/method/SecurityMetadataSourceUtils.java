package org.springframework.security.access.method;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.ConfigAttribute;

import java.lang.reflect.Method;
import java.util.Collection;

public class SecurityMetadataSourceUtils {

    public static Collection<ConfigAttribute> findAttributes(@NotNull AbstractFallbackMethodSecurityMetadataSource metadataSource, Method method, Class<?> clazz) {
        return metadataSource.findAttributes(method, clazz);
    }

    public static Collection<ConfigAttribute> findAttributes(@NotNull AbstractFallbackMethodSecurityMetadataSource metadataSource, Class<?> clazz) {
        return metadataSource.findAttributes(clazz);
    }

}
