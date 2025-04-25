package com.testservice.model;

import com.testservice.annotation.Test;
import com.testservice.annotation.TestClass;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Loader {
    static final String TEST_DATA_DIR = "testdata";
    static final String TEST_CONFIG_FILE = "global.json";

    public static Map<String, Object> parseJsonObject(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return JSON.parseObject(content, new TypeReference<Map<String, Object>>() {
        });
    }

    public static List<Map<String, Object>> parseJsonArray(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return JSON.parseObject(content, new TypeReference<List<Map<String, Object>>>() {
        });
    }

    private static List<Map<String, Object>> loadTestData(Class<?> clazz, String className, String methodName) {
        String dataFilePath = String.format("%s/%s/%s.json", TEST_DATA_DIR, className, methodName);
        List<Map<String, Object>> list = Collections.emptyList();
        URL resourceUrl = clazz.getClassLoader().getResource(dataFilePath);
        if (resourceUrl != null) {
            try {
                list = parseJsonArray(resourceUrl.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(className + "/" + methodName + " data file not found!");
        }
        return list;
    }

    private static Map<String, Object> loadTestConfig(Class<?> clazz, String className) {
        String configFilePath = String.format("%s/%s/%s", TEST_DATA_DIR, className, TEST_CONFIG_FILE);
        Map<String, Object> config = Collections.emptyMap();
        URL resourceUrl2 = clazz.getClassLoader().getResource(configFilePath);
        if (resourceUrl2 != null) {
            try {
                config = parseJsonObject(resourceUrl2.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(className + " config file not found!");
        }
        return config;
    }

    public static List<TestSuite> loadTestCases(String packageName) {
        System.out.println("==================================== 加载测试用例 ====================================");
        // 定义注解类
        Class<TestClass> testClass = TestClass.class;
        Class<Test> testMethod = Test.class;

        // 创建Reflections实例
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName)) // 设置扫描的包路径"com.example"
                .setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner()));

        // 获取带有类注解的类
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(testClass);

        List<TestSuite> testSuites = new ArrayList<TestSuite>();
        TestClass classAnnotation = null;
        List<TestCase> testCases = null;
        for (Class<?> clazz : annotatedClasses) {
            testCases = new ArrayList<TestCase>();

            classAnnotation = clazz.getAnnotation(testClass);
            System.out.println("找到测试类: " + clazz.getName() + " name:" + classAnnotation.name());

            // 获取类中带有方法注解的方法
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(testMethod)) {
                    Test methodAnnotation = method.getAnnotation(testMethod);
                    System.out.println("找到测试方法: " + method.getName() +
                            " description:" + methodAnnotation.description() +
                            " priority:" + methodAnnotation.priority() + " tags:"
                            + Arrays.toString(methodAnnotation.tags()));

                    // 加载测试数据
                    List<Map<String, Object>> paramsList = loadTestData(clazz, classAnnotation.name(), method.getName());
                    // 加载测试配置（全局数据）
                    Map<String, Object> config = loadTestConfig(clazz, classAnnotation.name());
                    if (!paramsList.isEmpty()) {
                        int dataIndex = 0;
                        for (Map<String, Object> params : paramsList) {
                            TestCase testCase = new TestCase(clazz, method, paramsList, config,
                                    methodAnnotation.description(), methodAnnotation.priority(), methodAnnotation.tags(), dataIndex, classAnnotation.name());
                            dataIndex += 1;
                            testCases.add(testCase);
                        }
                    } else {
                        TestCase testCase = new TestCase(clazz, method, paramsList, config,
                                methodAnnotation.description(), methodAnnotation.priority(), methodAnnotation.tags(), 0, classAnnotation.name());
                        testCases.add(testCase);
                    }

                    // 构建测试用例对象

                }
            }
            TestSuite testSuite = new TestSuite(classAnnotation.name(), testCases);
            testSuites.add(testSuite);

        }
        return testSuites;
    }
}