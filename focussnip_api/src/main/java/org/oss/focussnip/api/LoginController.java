package org.oss.focussnip.api;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/doLogin")
    public BaseResponse<JWTToken> login(@Valid @RequestBody UserLoginDto userLoginDto) throws Exception {
        // todo: 验证码

        Users user = userService.getByUsername(userLoginDto.getUsername());
        if(BCryptUtil.matchesPassword(userLoginDto.getPassword(),user.getPassword())){
            String token = JWTUtil.generTokenByRS256(userLoginDto.getUsername());
            JWTToken jwtToken = new JWTToken(token);
            return BaseResponse.getSuccessResponse(jwtToken);
        }
        // 参数异常
        throw new BusinessErrorException(BusinessMsgEnum.PARMETER_EXCEPTION);
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
        if(userService.save(user)){
            String token = JWTUtil.generTokenByRS256(userLoginDto.getUsername());
            JWTToken jwtToken = new JWTToken(token);
            return BaseResponse.getSuccessResponse(jwtToken);
        }
        // 参数异常
        throw new BusinessErrorException(BusinessMsgEnum.PARMETER_EXCEPTION);
    }

}
