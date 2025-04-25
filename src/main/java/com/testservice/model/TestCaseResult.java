package com.testservice.model;

import lombok.Data;

@Data
public class TestCaseResult {
    String moduleName;
    String className;
    String methodName;
    long startTime;
    long endTime;
    String status;
    String errMsg;
}
