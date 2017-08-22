package org.mdlp.core.mvc;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParentController {

    @NotNull Class<?> value();

}
