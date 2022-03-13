package org.oss.focussnip.exception;

import org.oss.focussnip.common.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 拦截业务异常，返回业务异常信息
     * @param ex
     * @return
     */
    @ExceptionHandler(BusinessErrorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse handleBusinessError(BusinessErrorException ex) {
        String code = ex.getCode();
        String message = ex.getMessage();
        logger.warn("业务异常",ex);
        return BaseResponse.getErrorResponse(code, message);
    }

    /**
     * 空指针异常
     * @param ex NullPointerException
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse handleTypeMismatchException(NullPointerException ex) {
        logger.error("空指针异常，{}", ex.getMessage());
        ex.printStackTrace(System.out);
        return BaseResponse.getErrorResponse("500", "空指针异常了");
    }

    /**
     * 系统异常 预期以外异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse handleUnexpectedServer(Exception ex) {
        logger.error("系统异常：", ex);
        return BaseResponse.getErrorResponse(BusinessMsgEnum.UNEXPECTED_EXCEPTION);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse RequestMethodNotSupportedServer(Exception ex) {
        logger.error("请求方式异常：", ex);
        return BaseResponse.getErrorResponse(BusinessMsgEnum.REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public BaseResponse handleBindException(BindException e) {
        logger.error("参数验证错误：", e);
        return BaseResponse.getErrorResponse(BusinessMsgEnum.PARMETER_EXCEPTION);
    }
}