package org.oss.focussnip.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

@Data
public class JWTToken implements AuthenticationToken {

    // 密钥
    private String token;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long expireAt;

    public JWTToken(String token) {
        this.token = token;
    }

    public JWTToken(String token, Long expireAt) {
        this.token = token;
        this.expireAt = expireAt;
    }

    @Override
    @JsonIgnore
    public Object getPrincipal() {
        return token;
    }

    @Override
    @JsonIgnore
    public Object getCredentials() {
        return token;
    }

}
