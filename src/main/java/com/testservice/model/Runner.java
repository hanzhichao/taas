package com.testservice.model;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class Runner {
    final static Logger logger = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        List<TestSuite> testSuites = Loader.loadTestCases("com.example");
        logger.info("==================================== 执行测试 ====================================");
        for (TestSuite testSuite: testSuites) {
            logger.info("测试：{}", testSuite.name);
            TestResult testResult = testSuite.runTest();
            logger.info(JSON.toJSONString(testResult));
        }
    }

    public void runTestCase(TestCase testCase){

    }
}
