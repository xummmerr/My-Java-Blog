package com.xummmerr.common; // 暂时放在 entity 包里方便你找

import lombok.Data;

@Data
public class Result<T> {
    private int code; // 200表示成功，500表示失败
    private String msg; // 提示信息
    private T data; // 返回的数据

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "操作成功";
        result.data = data;
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.code = 500;
        result.msg = msg;
        return result;
    }
}