package org.mdlp.core.security;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.method.ExpressionBasedAnnotationAttributeFactory;
import org.springframework.security.access.method.AbstractFallbackMethodSecurityMetadataSource;
import org.springframework.security.access.prepost.*;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PrePostAnnotationFallbackSecurityMetadataSource extends AbstractFallbackMethodSecurityMetadataSource {

    private final ExpressionBasedAnnotationAttributeFactory attributeFactory;

    public PrePostAnnotationFallbackSecurityMetadataSource(ExpressionBasedAnnotationAttributeFactory attributeFactory) {
        this.attributeFactory = attributeFactory;
    }

    @NotNull
    @Override
    protected Collection<ConfigAttribute> findAttributes(@NotNull Class<?> targetClass) {
        if (targetClass == Object.class) return Collections.emptyList();

        logger.trace("Looking for Pre/Post annotations for target class '" + targetClass + "'");
        PreFilter preFilter = findAnnotation(targetClass, PreFilter.class);
        PreAuthorize preAuthorize = findAnnotation(targetClass, PreAuthorize.class);
        PostFilter postFilter = findAnnotation(targetClass, PostFilter.class);
        PostAuthorize postAuthorize = findAnnotation(targetClass, PostAuthorize.class);

        if (preFilter == null && preAuthorize == null && postFilter == null && postAuthorize == null) {
            logger.trace("No expression annotations found");
            return Collections.emptyList();
        }

        String preFilterAttribute = preFilter == null ? null : preFilter.value();
        String filterObject = preFilter == null ? null : preFilter.filterTarget();
        String preAuthorizeAttribute = preAuthorize == null ? null : preAuthorize.value();
        String postFilterAttribute = postFilter == null ? null : postFilter.value();
        String postAuthorizeAttribute = postAuthorize == null ? null : postAuthorize.value();

        ArrayList<ConfigAttribute> attrs = new ArrayList<>(2);

        PreInvocationAttribute pre = attributeFactory.createPreInvocationAttribute(preFilterAttribute, filterObject, preAuthorizeAttribute);
        if (pre != null) attrs.add(pre);

        PostInvocationAttribute post = attributeFactory.createPostInvocationAttribute(postFilterAttribute, postAuthorizeAttribute);
        if (post != null) attrs.add(post);

        attrs.trimToSize();

        return attrs;
    }

    @NotNull
    @Override
    protected Collection<ConfigAttribute> findAttributes(@NotNull Method method, Class<?> targetClass) {
        if (method.getDeclaringClass() == Object.class) return Collections.emptyList();

        logger.trace("Looking for Pre/Post annotations for method '" + method.getName() + "' on target class '" + targetClass + "'");
        PreFilter preFilter = findAnnotation(method, targetClass, PreFilter.class);
        PreAuthorize preAuthorize = findAnnotation(method, targetClass, PreAuthorize.class);
        PostFilter postFilter = findAnnotation(method, targetClass, PostFilter.class);
        PostAuthorize postAuthorize = findAnnotation(method, targetClass, PostAuthorize.class);

        if (preFilter == null && preAuthorize == null && postFilter == null && postAuthorize == null) {
            logger.trace("No expression annotations found");
            return Collections.emptyList();
        }

        String preFilterAttribute = preFilter == null ? null : preFilter.value();
        String filterObject = preFilter == null ? null : preFilter.filterTarget();
        String preAuthorizeAttribute = preAuthorize == null ? null : preAuthorize.value();
        String postFilterAttribute = postFilter == null ? null : postFilter.value();
        String postAuthorizeAttribute = postAuthorize == null ? null : postAuthorize.value();

        ArrayList<ConfigAttribute> attrs = new ArrayList<>(2);

        PreInvocationAttribute pre = attributeFactory.createPreInvocationAttribute(preFilterAttribute, filterObject, preAuthorizeAttribute);
        if (pre != null) attrs.add(pre);

        PostInvocationAttribute post = attributeFactory.createPostInvocationAttribute(postFilterAttribute, postAuthorizeAttribute);
        if (post != null) attrs.add(post);

        attrs.trimToSize();

        return attrs;
    }

    @Nullable
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    private <A extends Annotation> A findAnnotation(@NotNull Method method, Class<?> targetClass, Class<A> annotationClass) {
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        A annotation = AnnotationUtils.findAnnotation(specificMethod, annotationClass);

        if (annotation != null) {
            logger.debug(annotation + " found on specific method: " + specificMethod);
            return annotation;
        }

        if (specificMethod != method) {
            annotation = AnnotationUtils.findAnnotation(method, annotationClass);

            if (annotation != null) {
                logger.debug(annotation + " found on: " + method);
                return annotation;
            }
        }

        return null;
    }

    private <A extends Annotation> A findAnnotation(@NotNull Class<?> targetClass, Class<A> annotationClass) {
        A annotation = AnnotationUtils.findAnnotation(targetClass, annotationClass);
        if (annotation != null) {
            logger.debug(annotation + " found on: " + targetClass.getName());
            return annotation;
        }

        return null;
    }

}
