package com.testservice.annotation;

import com.testservice.config.TestServiceConfig;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(TestServiceConfig.class)
public @interface TestService {
}
