package com.testservice.controller;

import com.alibaba.fastjson.JSON;
import com.testservice.model.TestCaseResult;
import com.testservice.model.TestResult;
import com.testservice.utils.TestSuitesHolder;
import com.testservice.model.TestCase;
import com.testservice.model.TestSuite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    @Autowired
    private TestSuitesHolder testSuitesHolder;

    @GetMapping("/default")
    public String defaultEndpoint(@RequestParam(required = false, defaultValue = "World") String name) {
        return "Hello, " + name + "! This is a default web interface.";
    }

    @GetMapping("/api/run/{module}")
    public ResponseEntity<TestResult> runSuite(@PathVariable String module) {
        TestSuite testSuite = testSuitesHolder.getTestSuite(module);
        TestResult testResult = null;
        if (testSuite != null) {
            testResult = testSuite.runTest();
        }
        return ResponseEntity.ok(testResult);
    }


    @GetMapping("/api/run/{module}/{testcase}")
    public ResponseEntity<TestCaseResult> runCase(@PathVariable String module, @PathVariable String testcase) {
        TestCase testCase = testSuitesHolder.getTestCase(module, testcase);
        TestCaseResult testCaseResult = null;
        if (testcase != null) {
            testCaseResult = testCase.runTest();
        }
        return ResponseEntity.ok(testCaseResult);

    }

    @GetMapping("/api/data/{module}/{testcase}")
    public String data(@PathVariable String module, @PathVariable String testcase) {
        TestCase testCase = testSuitesHolder.getTestCase(module, testcase);
        if (testcase != null) {
            return JSON.toJSONString(testCase.paramsList);
        }
        return "未找到：" + module + "/" + testcase;

    }

    @GetMapping("/api/config/{module}/{testcase}")
    public String config(@PathVariable String module, @PathVariable String testcase) {
        TestCase testCase = testSuitesHolder.getTestCase(module, testcase);
        if (testcase != null) {
            return JSON.toJSONString(testCase.config);
        }
        return "未找到：" + module + "/" + testcase;

    }

}
