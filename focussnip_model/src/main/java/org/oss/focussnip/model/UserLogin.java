package org.oss.focussnip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserLogin extends BaseModel {
    private Long phone;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
}
