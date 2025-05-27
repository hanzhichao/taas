package com.testservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestSuite {
    public String name;
    public List<TestCase> testcases;
    public Map<String, Object> config;

    public TestSuite(String name, List<TestCase> testCases, Map<String, Object> config) {
        this.name = name;
        this.testcases = testCases;
        this.config = config;
    }

    public TestResult runTest() {
        TestResult testResult = new TestResult();
//        testResult.suite = name;
        testResult.startTime = System.currentTimeMillis();

        testResult.isSuccess = true;
        testResult.details = new ArrayList<>();
        for (TestCase testCase : testcases) {
            TestCaseResult testCaseResult = testCase.runTest();
            testResult.totalRun += 1;
            testResult.details.add(testCaseResult);
            if (testCaseResult.status.equals("pass")) {
                testResult.passed += 1;
            } else if (testCaseResult.status.equals("fail")) {
                testResult.failed += 1;
            } else {
                testResult.errors += 1;
            }
        }
        if (testResult.errors != 0 || testResult.failed != 0) {
            testResult.isSuccess = false;
        }
        testResult.endTime = System.currentTimeMillis();
        return testResult;
    }
}
