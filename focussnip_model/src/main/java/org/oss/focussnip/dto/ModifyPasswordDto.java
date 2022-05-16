package org.oss.focussnip.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ModifyPasswordDto {
    @NotBlank
    @Length(min=2, max=20, message = "长度异常")
    private String username;

    @NotBlank
    @Length(min=6, max=15, message = "长度异常")
    @Pattern(regexp="^[a-zA-Z0-9|_]+$", message = "密码含非法字符")
    private String newPassword;

    private String phone;
}
