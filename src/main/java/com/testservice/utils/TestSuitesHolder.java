package com.testservice.utils;

import com.testservice.annotation.TestService;
import com.testservice.model.Loader;
import com.testservice.model.TestCase;
import com.testservice.model.TestSuite;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TestSuitesHolder implements ApplicationContextAware {
    private List<TestSuite> testSuites;


    public TestSuite getTestSuite(String suiteName) {
        System.out.println("筛选测试套件：" + suiteName);
        for (TestSuite testSuite: testSuites){
            if (suiteName.equals(testSuite.name)) {
                return testSuite;
            }
        };
        return null;
    }

    public TestCase getTestCase(String suiteName, String caseName) {
        System.out.println("筛选用例：" + suiteName + "/" + caseName);
        for (TestSuite testSuite: testSuites){
            if (suiteName.equals(testSuite.name)) {
                for (TestCase testCase : testSuite.testCases) {
                    if (caseName.equals(testCase.name)) {
                        return testCase;
                    }
                }
            }
        };
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取所有被@TestService注解的Bean
        String packageName = ""; // 默认
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(TestService.class);
        if (!beans.isEmpty()) {
            Object mainBean = beans.values().iterator().next();
            packageName = mainBean.getClass().getPackage().getName();
        }
        testSuites = Loader.loadTestCases(packageName);
    }
}
