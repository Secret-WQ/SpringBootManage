package com.wq.exception;


import com.wq.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandlers {


    /*如果抛出的的是ServiceException，则调用该方法
     *@param serviceExcetion 业务异常
     *@return Result
     */

    @ExceptionHandler(ServiceExcetion.class)
    @ResponseBody
    public Result handle(ServiceExcetion serviceExcetion) {

        return Result.error(serviceExcetion.getCode(), serviceExcetion.getMessage());
    }

}
