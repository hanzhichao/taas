package com.testservice.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;


public class TestCase {
    public String moduleName;
    Class<?> clazz;
    Method method;
    public List<Map<String, Object>> paramsList;
    int dataIndex;
    public Map<String, Object> config;

    public String name;
    String description;
    int priority;
    String[] tags;

    public TestCase(Class<?> clazz, Method method, List<Map<String, Object>> paramsList,
                    Map<String, Object> config, String description,
                    int priority, String[] tags, int dataIndex, String moduleName) {
        this.clazz = clazz;
        this.method = method;

        this.paramsList = paramsList;
        this.config = config;

        this.name = method.getName();
        this.description = description;
        this.priority = priority;
        this.tags = tags;
        this.dataIndex = dataIndex;
        this.moduleName = moduleName;
    }

    public void callMethod(Object instance, Parameter[] parameters) throws Exception {
        if (parameters.length == 0) {
            method.invoke(instance);
        } else if (parameters.length == 1) {
            method.invoke(instance, config);

        } else if (parameters.length == 2) {
            method.invoke(instance, config, paramsList.get(dataIndex));
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
        testCaseResult.methodName = method.getName();
        testCaseResult.moduleName = moduleName;

        testCaseResult.startTime = System.currentTimeMillis();
        System.out.println("-------- 运行测试用例: " + clazz.getName() + "." + method.getName() + " dataIndex: " + dataIndex + " --------");
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
