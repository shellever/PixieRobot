package com.shellever.pixierobot.bean;

/**
 * Created by linuxfor on 8/13/2016.
 */
//图灵机器人返回的数据对象封装
public class Result {
    private int code;
    private String text;

    public Result() {
    }

    public Result(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
