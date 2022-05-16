package org.oss.focussnip.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.dto.UserLoginDto;
import org.oss.focussnip.exception.BusinessErrorException;
import org.oss.focussnip.exception.BusinessMsgEnum;
import org.oss.focussnip.model.Users;
import org.oss.focussnip.security.JWTToken;
import org.oss.focussnip.service.UserService;
import org.oss.focussnip.utils.BCryptUtil;
import org.oss.focussnip.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api("登录注册，找回密码")
@RestController
@CrossOrigin(origins = "*")
public class LoginController {
    @Autowired
    private UserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/doLogin")
    public BaseResponse<JWTToken> login(@Valid @RequestBody UserLoginDto userLoginDto) throws Exception {
        // todo: 验证码

        Users user = userService.getByUsername(userLoginDto.getUsername());
        if(user == null){
            throw new BusinessErrorException(BusinessMsgEnum.USER_NOT_EXIST_EXCEPTION);
        }
        if(BCryptUtil.matchesPassword(userLoginDto.getPassword(),user.getPassword())){
            String token = JWTUtil.generTokenByRS256(userLoginDto.getUsername());
            JWTToken jwtToken = new JWTToken(token , userLoginDto.getUsername());
            return BaseResponse.getSuccessResponse(jwtToken);
        }
        // 参数异常
        throw new BusinessErrorException(BusinessMsgEnum.USER_LOGIN_EXCEPTION);
    }

    @PostMapping("/register")
    public BaseResponse<JWTToken> register(@Valid @RequestBody UserLoginDto userLoginDto) throws Exception {
        // todo: 验证码
        Users user = userService.getByUsername(userLoginDto.getUsername());
        if(user != null){
            throw new BusinessErrorException(BusinessMsgEnum.USER_EXIST_EXCEPTION);
        }
        user = new Users();
        user.setUsername(userLoginDto.getUsername());
        user.setPassword(BCryptUtil.EncodePassword(userLoginDto.getPassword()));
        user.setPassword(userLoginDto.getPhone());
        if(userService.save(user)){
            String token = JWTUtil.generTokenByRS256(userLoginDto.getUsername());
            JWTToken jwtToken = new JWTToken(token , userLoginDto.getUsername());
            return BaseResponse.getSuccessResponse(jwtToken);
        }
        // 参数异常
        throw new BusinessErrorException(BusinessMsgEnum.PARMETER_EXCEPTION);
    }
}
