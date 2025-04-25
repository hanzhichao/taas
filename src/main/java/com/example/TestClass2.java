package com.example;

import com.testservice.annotation.Test;
import com.testservice.annotation.TestClass;

import java.util.Map;

@TestClass(name ="module2")
public class TestClass2 {
    @Test(description = "测试用例2", priority = 2, tags = {})
    public void testSomething2(Map<String, Object> testConfig){
        String env = (String) testConfig.get("env");
        String tester = (String) testConfig.get("tester");
        System.out.println("全局变量：env: " + env + " tester: " + tester);

        System.out.println("Test something2...");
    }

}
