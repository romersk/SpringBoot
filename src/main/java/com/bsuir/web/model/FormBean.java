package com.bsuir.web.model;

import java.util.List;

public class FormBean {

    private List<List<Long>> matrix;

    FormBean() { }

    public FormBean(List<List<Long>> matrix) {
        this.matrix = matrix;
    }

    public List<List<Long>> getMatrix() {
        return matrix;
    }

    public void setMatrix(List<List<Long>> matrix) {
        this.matrix = matrix;
    }
}
