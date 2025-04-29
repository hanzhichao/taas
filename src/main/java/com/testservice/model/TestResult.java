package com.testservice.model;


import lombok.Data;

import java.util.List;

@Data
public class TestResult {
    String suite;
    int totalRun;
    int passed;
    int failed;
    int errors;
    long startTime;
    long endTime;
    boolean isSuccess;
    List<TestCaseResult> details;

}



