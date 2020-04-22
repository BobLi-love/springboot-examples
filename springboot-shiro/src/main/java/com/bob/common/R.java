package com.bob.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class R implements Serializable {

    private int code;

    private String msg;

    private Object data;

    public static R success() {
        return resultData(200, "成功", null);
    }

    public static R success(Object data) {
       return resultData(200, "成功", data);
    }

    public static R success(Object data, String msg) {
        return resultData(200, msg, data);
    }

    public static R fail() {
        return resultData(500, "失败", null);
    }

    public static R fail(Object data) {
        return resultData(500, "失败", data);
    }

    public static R fail(int code, String msg) {
        return resultData(code, msg, null);
    }
    public static R fail(int code, String msg, Object data) {
        return resultData(code, msg, data);
    }

    private static R resultData(int code, String msg, Object data) {
        R resultData = new R();
        resultData.setCode(code);
        resultData.setMsg(msg);
        resultData.setData(data);
        return resultData;
    }
}