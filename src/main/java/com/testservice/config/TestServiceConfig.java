package com.testservice.config;

import com.testservice.controller.TestController;
import com.testservice.utils.TestSuitesHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestServiceConfig {
    @Bean
    public TestController testController() {
        return new TestController();
    }

    @Bean
    public TestSuitesHolder testSuitesHolder() { return new TestSuitesHolder(); }
}
