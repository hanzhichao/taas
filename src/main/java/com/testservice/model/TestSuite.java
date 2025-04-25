package com.testservice.model;

import java.util.ArrayList;
import java.util.List;

public class TestSuite {
    public String name;
    public List<TestCase> testCases;

    public TestSuite(String name, List<TestCase> testCases) {
        this.name = name;
        this.testCases = testCases;
    }

    public TestResult runTest() {
        TestResult testResult = new TestResult();
        testResult.suiteName = name;
        testResult.startTime = System.currentTimeMillis();

        testResult.isSuccess = true;
        testResult.details = new ArrayList<>();
        for (TestCase testCase : testCases) {
            TestCaseResult testCaseResult = testCase.runTest();
            testResult.totalRun += 1;
            testResult.details.add(testCaseResult);
            if (testCaseResult.status == "pass") {
                testResult.passed += 1;
            } else if (testCaseResult.status == "fail") {
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
