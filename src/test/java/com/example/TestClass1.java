package com.example;
import com.testservice.annotation.Test;
import com.testservice.annotation.Suite;
import org.junit.Assert;

import java.util.Map;

@Suite(name = "suite1")
public class TestClass1 {
    @Test(description = "测试用例1", priority = 1, tags = {"demo"}, author = "hzc")
    public void testSomething(Map<String, Object> testConfig, Map<String, Object> params) {
        String username = (String) params.get("username");
        String pwd = (String) params.get("pwd");
        System.out.println("用例数据：username: " + username + " pwd: " + pwd);
        System.out.println("Test something...");
    }

    @Test(description = "测试用例3", priority = 1, tags = {"demo"}, author = "kevin")
    public void testSomething3() {
        System.out.println("Doing something3...");
        Assert.fail("示例断言");
    }

}
