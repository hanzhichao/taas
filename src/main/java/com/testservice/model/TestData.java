package com.testservice.model;

import java.util.Map;

public class TestData {
    public String name;
    public String suite;
    public String description;
    public int priority;
    public String[] tags;
    public int index;
    public Map<String, String> params;

    public TestData(String name, String suite, String description, int priority, String[] tags, int index, Map<String, String> params){
        this.name= name;
        this.suite = suite;
        this.description = description;
        this.priority = priority;
        this.tags = tags;
        this.index = index;
        this.params = params;
    }
}
