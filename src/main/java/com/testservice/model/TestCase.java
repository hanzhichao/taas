package com.testservice.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public class TestCase {
    final static Logger logger = LoggerFactory.getLogger(TestCase.class);

    public String name;
    public String suite;
    public String description;
    public String author;
    public int priority;
    public String[] tags;
    public List<Map<String, String>> params;

    private Class<?> clazz;
    private Method method;
    private int dataIndex;
    private Map<String, Object> config;

    public TestCase(Class<?> clazz, Method method, List<Map<String, String>> params,
                    Map<String, Object> config, String description,
                    int priority, String[] tags, int dataIndex, String suite, String author) {
        this.clazz = clazz;
        this.method = method;

        this.params = params;
        this.config = config;

        this.name = method.getName();
        this.description = description;
        this.priority = priority;
        this.tags = tags;
        this.dataIndex = dataIndex;
        this.suite = suite;
        this.author = author;
    }

    public TestData getTestData(int index) {
        Map<String, String> params = this.params.get(index);
        return new TestData(name, suite, description, priority, tags, index, params);
    }

    public void callMethod(Object instance, Parameter[] parameters) throws Exception {
        if (parameters.length == 0) {
            method.invoke(instance);
        } else if (parameters.length == 1) {
            method.invoke(instance, config);

        } else if (parameters.length == 2) {
            method.invoke(instance, config, params.get(dataIndex));
        }
    }

    public static String getErrMsg(Exception e) {
        StringWriter sw = new StringWriter();
        e.getCause().printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public TestCaseResult runTest() {
        TestCaseResult testCaseResult = new TestCaseResult();
        testCaseResult.className = clazz.getName();
        testCaseResult.method = method.getName();
        testCaseResult.suite = suite;

        testCaseResult.startTime = System.currentTimeMillis();
        logger.info("-------- 运行测试用例: {}.{} dataIndex: {} --------", clazz.getName(), method.getName(), dataIndex);
        String status = "pass"; // 0 调用出错， 1，成功，2，失败，3，出错
        String errMsg = "";

        try {
            Object instance = clazz.getDeclaredConstructor().newInstance(); // 创建类的实例
            method.setAccessible(true); // 确保可以调用私有方法
            Parameter[] parameters = method.getParameters();
            callMethod(instance, parameters);
        } catch (InvocationTargetException e) {
            status = "fail";
            errMsg = getErrMsg(e);
        } catch (Exception e) {
            status = "error";
            errMsg = e.getMessage();
        }

        testCaseResult.endTime = System.currentTimeMillis();
        testCaseResult.status = status;
        testCaseResult.errMsg = errMsg;
        return testCaseResult;

    }
}
