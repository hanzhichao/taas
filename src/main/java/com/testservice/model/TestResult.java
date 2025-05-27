package com.testservice.model;


import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Data
public class TestResult {
    final static Logger logger = LoggerFactory.getLogger(TestResult.class);

//    String suite;
    int totalRun;
    int passed;
    int failed;
    int errors;
    long startTime;
    long endTime;
    boolean isSuccess;
    List<TestCaseResult> details;

    public void onSuiteStart(){

    }

    public void onSuiteEnd(){

    }

    public void onCaseStart(){

    }

    public void onCaseEnd(){

    }

}



