package org.oss.focussnip.api;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.dto.ModifyPasswordDto;
import org.oss.focussnip.exception.BusinessErrorException;
import org.oss.focussnip.exception.BusinessMsgEnum;
import org.oss.focussnip.model.Users;
import org.oss.focussnip.service.UserService;
import org.oss.focussnip.utils.BCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api("用户api")
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("/获取当前用户信息")
    @GetMapping("/user")
    public BaseResponse<Users> user() throws Exception {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        Users user = userService.getByUsername(username);
        if(user==null){
            throw new BusinessErrorException(BusinessMsgEnum.USER_EXIST_EXCEPTION);
        }
        return BaseResponse.getSuccessResponse(user);
    }

    @ApiOperation("/更改当前用户的信息")
    @PutMapping("/user")
    public BaseResponse<Users> putUser(Users user) throws Exception {
        UpdateWrapper<Users> condition = new UpdateWrapper<>();
        condition.eq("id",user.getId());
        boolean flag = !userService.update(user,condition);
        if(flag){
            throw new BusinessErrorException(BusinessMsgEnum.USER_EXIST_EXCEPTION);
        }
        return BaseResponse.getSuccessResponse(user);
    }

    @ApiOperation("/获取某位用户的信息")
    @GetMapping("/user/{id}")
    public BaseResponse<Users> getUser(@PathVariable Integer id) throws Exception {
        Users user = userService.getById(id);
        if(user==null){
            throw new BusinessErrorException(BusinessMsgEnum.USER_EXIST_EXCEPTION);
        }
        return BaseResponse.getSuccessResponse(user);
    }

    @ApiOperation("/更改用户密码")
    @PutMapping("/password")
    public BaseResponse modifyPassword(@Valid @RequestBody ModifyPasswordDto modifyPasswordDto) throws Exception {
        Users user = userService.getByUsername(modifyPasswordDto.getUsername());
        if (StringUtils.isBlank(user.getPhone()) || !user.getPhone().equals(modifyPasswordDto.getPhone())){
            throw new BusinessErrorException(BusinessMsgEnum.PHONEVERIFICATIONERROR);
        }
        user.setPassword(BCryptUtil.EncodePassword(modifyPasswordDto.getNewPassword()));
        UpdateWrapper<Users> condition = new UpdateWrapper<>();
        condition.eq("username",user.getUsername());
        boolean flag = userService.update(user,condition);
        if(!flag){
            throw new BusinessErrorException(BusinessMsgEnum.PASSWORDRESETFAILED);
        }
        return BaseResponse.getSuccessResponse("修改成功");
    }
}
