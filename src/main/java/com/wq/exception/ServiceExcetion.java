package com.wq.exception;

import com.wq.common.Constants;
import lombok.Data;

/*
* 自定义异常
* */
@Data
public class ServiceExcetion extends RuntimeException {
    private Constants.CODE code;

    public ServiceExcetion(Constants.CODE code, String msg) {
        super(msg);
        this.code = code;
    }


}
