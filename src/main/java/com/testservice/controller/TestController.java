package com.testservice.controller;

import com.alibaba.fastjson.JSON;
import com.testservice.model.*;
import com.testservice.utils.TestSuitesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class TestController {
    final static Logger logger = LoggerFactory.getLogger(TestController.class);


    @Autowired
    private TestSuitesHolder testSuitesHolder;

    @GetMapping("/echo")
    public String echo(@RequestParam(required = false, defaultValue = "Hi") String name) {
        return "echo, " + name + "! This is a default web interface.";
    }

    @GetMapping("/api/suites")
    public ResponseEntity<List<TestSuite>> getSuiteList() {
        List<TestSuite> testSuites = testSuitesHolder.getTestSuites();
        return ResponseEntity.ok(testSuites);
    }

    @GetMapping("/api/suites/{suiteName}")
    public ResponseEntity<TestSuite> getSuite(@PathVariable String suiteName) {
        TestSuite testSuite = testSuitesHolder.getTestSuite(suiteName);
        return ResponseEntity.ok(testSuite);
    }

    @GetMapping("/api/suites/{suite}/{testcase}")
    public ResponseEntity<TestCase> getTestCase(@PathVariable String suite, @PathVariable String testcase) {
        TestCase testCase = testSuitesHolder.getTestCase(suite, testcase);
        return ResponseEntity.ok(testCase);
    }

    @GetMapping("/api/suites/{suite}/{testcase}/{index}")
    public ResponseEntity<TestData> getTestData(@PathVariable String suite, @PathVariable String testcase, @PathVariable int index) {
        TestCase testCase = testSuitesHolder.getTestCase(suite, testcase);
        TestData testData = testCase.getTestData(index);
        return ResponseEntity.ok(testData);
    }

    @GetMapping("/run/suites")
    public ResponseEntity<List<TestResult>> runAll() {
        List<TestSuite> testSuites = testSuitesHolder.getTestSuites();
        List<TestResult> testResults = new ArrayList<>();
        for (TestSuite testSuite: testSuites) {
            TestResult testResult = testSuite.runTest();
            testResults.add(testResult);
        }
        return ResponseEntity.ok(testResults);
    }


    @GetMapping("/run/suites/{suite}")
    public ResponseEntity<TestResult> runTestSuite(@PathVariable String suite) {
        TestSuite testSuite = testSuitesHolder.getTestSuite(suite);
        TestResult testResult = null;
        if (testSuite != null) {
            testResult = testSuite.runTest();
        }
        return ResponseEntity.ok(testResult);
    }

    @GetMapping("/run/suites/{suite}/{testcase}")
    public ResponseEntity<TestCaseResult> runTestCase(@PathVariable String suite, @PathVariable String testcase) {
        TestCase testCase = testSuitesHolder.getTestCase(suite, testcase);
        TestCaseResult testCaseResult = null;
        if (testcase != null) {
            testCaseResult = testCase.runTest();
        }
        return ResponseEntity.ok(testCaseResult);
    }

}
