package com.testservice.model;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class Runner {
    public static void main(String[] args) {
        List<TestSuite> testSuites = Loader.loadTestCases("com.example");

        System.out.println("==================================== 执行测试 ====================================");
        for (TestSuite testSuite: testSuites) {
            System.out.println("测试：" + testSuite.name);
            TestResult testResult = testSuite.runTest();
            System.out.println(JSON.toJSONString(testResult));
        }

    }
}
