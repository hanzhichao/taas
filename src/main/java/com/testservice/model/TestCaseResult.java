package com.testservice.model;

import lombok.Data;

@Data
public class TestCaseResult {
    String suite;
    String className;
    String method;
    long startTime;
    long endTime;
    String status;
    String errMsg;
}
