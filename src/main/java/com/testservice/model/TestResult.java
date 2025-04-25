package com.testservice.model;


import lombok.Data;

import java.util.List;

@Data
public class TestResult {
    String suiteName;

    int totalRun;
    int passed;
    int failed;
    int errors;
    long startTime;
    long endTime;
    boolean isSuccess;
    List<TestCaseResult> details;

    public String getSuiteName(){
        return suiteName;
    }
}



