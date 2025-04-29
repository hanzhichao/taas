package com.testservice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Suite {
    String name();
    String description() default "";
    int priority() default -1;
    String author() default "";
    String[] tags() default {};
}
