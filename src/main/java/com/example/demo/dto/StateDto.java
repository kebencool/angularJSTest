package com.example.demo.dto;

public class StateDto {

    private int state;
    private String string;
    private Object object;


    public StateDto() {

    }

    public StateDto(int state, String string, Object object) {
        this.state = state;
        this.string = string;
        this.object = object;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
