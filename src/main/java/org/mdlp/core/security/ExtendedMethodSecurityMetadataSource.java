package org.mdlp.core.security;

import org.mdlp.core.mvc.ParentController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.method.AbstractFallbackMethodSecurityMetadataSource;
import org.springframework.security.access.method.AbstractMethodSecurityMetadataSource;
import org.springframework.security.access.method.SecurityMetadataSourceUtils;

import java.lang.reflect.Method;
import java.util.*;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;
import static org.springframework.util.ClassUtils.getUserClass;

public class ExtendedMethodSecurityMetadataSource extends AbstractMethodSecurityMetadataSource {

    private final List<AbstractFallbackMethodSecurityMetadataSource> sources;

    public ExtendedMethodSecurityMetadataSource(List<AbstractFallbackMethodSecurityMetadataSource> sources) {
        this.sources = sources;
    }

    @Nullable
    @Override
    public Collection<ConfigAttribute> getAttributes(@NotNull Method method, Class<?> targetClass) {
        if (Object.class == method.getDeclaringClass()) return Collections.emptyList();
        targetClass = getUserClass(targetClass);

        List<ConfigAttribute> attrs = null;
        for (AbstractFallbackMethodSecurityMetadataSource source : sources) {
            Collection<ConfigAttribute> curAttrs = SecurityMetadataSourceUtils.findAttributes(source, method, targetClass);
            if (null != curAttrs) {
                if (null == attrs) attrs = new ArrayList<>();
                attrs.addAll(curAttrs);
            }
        }

        for (AbstractFallbackMethodSecurityMetadataSource source : sources) {
            Collection<ConfigAttribute> curAttrs = SecurityMetadataSourceUtils.findAttributes(source, targetClass);
            if (null != curAttrs) {
                if (null == attrs) attrs = new ArrayList<>();
                attrs.addAll(curAttrs);
            }
        }

        for (AbstractFallbackMethodSecurityMetadataSource source : sources) {
            Class<?> curTargetClass = targetClass;
            while (true) {
                ParentController parentController = findMergedAnnotation(curTargetClass, ParentController.class);
                if (null == parentController) break;

                curTargetClass = parentController.value();
                Collection<ConfigAttribute> curAttrs = SecurityMetadataSourceUtils.findAttributes(source, curTargetClass);
                if (null != curAttrs) {
                    if (null == attrs) attrs = new ArrayList<>();
                    attrs.addAll(curAttrs);
                }
            }
        }

        if (null == attrs) return null;

        Collections.reverse(attrs);
        return new LinkedHashSet<>(attrs);
    }

    @NotNull
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> attrs = new HashSet<>();
        for (SecurityMetadataSource source : sources) {
            Collection<ConfigAttribute> curAttrs = source.getAllConfigAttributes();
            if (null != curAttrs) {
                attrs.addAll(curAttrs);
            }
        }

        return attrs;
    }

}
