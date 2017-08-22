package org.mdlp.core.mvc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;
import static org.springframework.util.ClassUtils.getUserClass;

public class ExtendedRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);

        if (null != info) {
            Class<?> curHandlerType = handlerType;
            Class<?> prevHandlerType;
            while (true) {
                ParentController parentController = findMergedAnnotation(curHandlerType, ParentController.class);
                if (null == parentController) break;

                prevHandlerType = curHandlerType;
                curHandlerType = getUserClass(parentController.value());

                RequestMapping requestMapping = findMergedAnnotation(curHandlerType, RequestMapping.class);
                if (null == requestMapping) {
                    throw new IllegalStateException("Super controller " + curHandlerType.getName() + " do not have RequestMapping annotation (child controller - " + prevHandlerType.getName() + ")");
                }
                RequestCondition<?> condition = getCustomTypeCondition(curHandlerType);
                RequestMappingInfo curInfo = createRequestMappingInfo(requestMapping, condition);
                if (null == curInfo) {
                    throw new IllegalStateException("RequestMappingInfo can't be created from super controller " + curHandlerType.getName() + " (child controller - " + prevHandlerType.getName() + ")");
                }

                info = curInfo.combine(info);
            }
        }

        return info;
    }

}
