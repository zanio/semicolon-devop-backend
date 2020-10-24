package com.semicolondevop.suite.model.techStack;

public enum TechStackType {
    VUE,
    REACT,
    PYTHON,
    JAVA,
    NODE;

    private String code;

     TechStackType(String code) {
        this.code = code;
    }

    TechStackType() {

    }

    public String getCode() {
        return code;
    }
}
