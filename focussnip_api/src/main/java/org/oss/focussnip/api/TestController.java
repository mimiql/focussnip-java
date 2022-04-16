package org.oss.focussnip.api;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.exception.BusinessErrorException;
import org.oss.focussnip.exception.BusinessMsgEnum;
import org.oss.focussnip.model.Users;
import org.oss.focussnip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    // RequiresAuthentication 必须经过过滤器 并已经成功登录
    @RequiresAuthentication
    // RequiresPermissions 必须经过过滤器 且有权限
    @RequiresPermissions("admin:all:all")
    @GetMapping("/jsonres")
    public BaseResponse<Users> testJsonIgnoreAndRes(){
        return BaseResponse.getSuccessResponse(userService.test());
    }

    @GetMapping("/exception")
    public BaseResponse<Character> testException(){
        return BaseResponse.getSuccessResponse(userService.test().getUsername().charAt(5));
    }

    @GetMapping("/throwException")
    public BaseResponse<Character> testThrowException(){
        throw new BusinessErrorException(BusinessMsgEnum.UNEXPECTED_EXCEPTION);
    }

    @GetMapping("/v2")
    public BaseResponse<Character> v2Exception(){
        throw new BusinessErrorException(BusinessMsgEnum.UNEXPECTED_EXCEPTION);
    }
}
