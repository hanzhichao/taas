package com.testservice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    // 定义注解的元素
    String description() default "";
    int priority() default 1;
    String[] tags() default {};
    int order() default  0;
    String author() default "";

}


