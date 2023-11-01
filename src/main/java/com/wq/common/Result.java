package com.wq.common;

/*
 * 接口统一返回类
 *
 * */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {


    private Constants.CODE code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result(Constants.CODE.CODE_200, "", null);
    }

    public static Result success(Object data) {
        return new Result(Constants.CODE.CODE_200, "操作成功", data);
    }

    public static Result success(Object data, String msg) {
        return new Result(Constants.CODE.CODE_200, msg, data);
    }

    public static Result error(Constants.CODE code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result error() {
        return new Result(Constants.CODE.CODE_500, "系统错误", null);
    }


}
