package org.oss.focussnip.api;

import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.exception.BusinessErrorException;
import org.oss.focussnip.exception.BusinessMsgEnum;
import org.oss.focussnip.model.UserLogin;
import org.oss.focussnip.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/jsonres")
    public BaseResponse<UserLogin> testJsonIgnoreAndRes(){
        return BaseResponse.getSuccessResponse(loginService.test());
    }

    @GetMapping("/exception")
    public BaseResponse<Character> testException(){
        return BaseResponse.getSuccessResponse(loginService.test().getUsername().charAt(5));
    }

    @GetMapping("/throwException")
    public BaseResponse<Character> testThrowException(){
        throw new BusinessErrorException(BusinessMsgEnum.UNEXPECTED_EXCEPTION);
    }
}
