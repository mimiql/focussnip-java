package org.oss.focussnip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
public class Users extends BaseModel {
    @NotBlank
    @Length(min=2, max=20)
    private String username;
    @JsonIgnore
    @NotBlank
    @Length(min=6, max=15)
    @Pattern(regexp="^[a-zA-Z0-9|_]+$")
    private String password;
    private Integer starNum;
    private Long roleId;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private Long starId;
}
