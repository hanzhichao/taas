package com.testservice.model;

import com.testservice.annotation.Test;
import com.testservice.annotation.Suite;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Loader {
    final static Logger logger = LoggerFactory.getLogger(Loader.class);

    static final String TEST_DATA_DIR = "testdata";
    static final String TEST_CONFIG_FILE = "global.json";

    public static Map<String, Object> parseJsonObject(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return JSON.parseObject(content, new TypeReference<Map<String, Object>>() {
        });
    }

    public static List<Map<String, String>> parseJsonArray(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return JSON.parseObject(content, new TypeReference<List<Map<String, String>>>() {
        });
    }

    private static List<Map<String, String>> loadTestData(Class<?> clazz, String className, String methodName) {
        String dataFilePath = String.format("%s/%s/%s.json", TEST_DATA_DIR, className, methodName);
        List<Map<String, String>> list = Collections.emptyList();
        URL resourceUrl = clazz.getClassLoader().getResource(dataFilePath);
        if (resourceUrl != null) {
            try {
                list = parseJsonArray(resourceUrl.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            logger.warn("{}/{} data file not found!", className, methodName);
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
            logger.warn("{} config file not found!", className);
        }
        return config;
    }

    public static List<TestSuite> loadTestCases(String packageName) {
        logger.info("==================================== 加载测试用例 ====================================");
        // 定义注解类
        Class<Suite> testClass = Suite.class;
        Class<Test> testMethod = Test.class;

        // 创建Reflections实例
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName)) // 设置扫描的包路径"com.example"
                .setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner()));

        // 获取带有类注解的类
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(testClass);

        List<TestSuite> testSuites = new ArrayList<TestSuite>();
        Suite classAnnotation = null;
        List<TestCase> testCases = null;
        for (Class<?> clazz : annotatedClasses) {
            testCases = new ArrayList<TestCase>();

            classAnnotation = clazz.getAnnotation(testClass);
            String suiteName = classAnnotation.name();
            logger.info("找到测试类: {} name:{}", clazz.getName(), classAnnotation.name());

            // 获取类中带有方法注解的方法
            Method[] methods = clazz.getDeclaredMethods();
            Map<String, Object> config = loadTestConfig(clazz, classAnnotation.name());
            for (Method method : methods) {
                if (method.isAnnotationPresent(testMethod)) {
                    Test methodAnnotation = method.getAnnotation(testMethod);
                    String methodName = method.getName();

                    String description = methodAnnotation.description();
                    int priority = methodAnnotation.priority();
                    String author = methodAnnotation.author();
                    String[] tags = methodAnnotation.tags();

                    logger.info("找到测试方法: {} description:{} priority:{} tags:{}", methodName, description, priority, Arrays.toString(tags));

                    // 加载测试数据
                    List<Map<String, String>> paramsList = loadTestData(clazz, suiteName, methodName);
                    // 加载测试配置（全局数据）

                    if (!paramsList.isEmpty()) {
                        int dataIndex = 0;
                        for (Map<String, String> params : paramsList) {
                            TestCase testCase = new TestCase(clazz, method, paramsList, config,description, priority, tags, dataIndex, suiteName, author);
                            dataIndex += 1;
                            testCases.add(testCase);
                        }
                    } else {
                        TestCase testCase = new TestCase(clazz, method, paramsList, config,description, priority, tags,0, suiteName, author);
                        testCases.add(testCase);
                    }
                    // 构建测试用例对象
                }
            }
            TestSuite testSuite = new TestSuite(classAnnotation.name(), testCases, config);
            testSuites.add(testSuite);

        }
        return testSuites;
    }
}