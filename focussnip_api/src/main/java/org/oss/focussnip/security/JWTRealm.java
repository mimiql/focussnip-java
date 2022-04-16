package org.oss.focussnip.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.oss.focussnip.model.Users;
import org.oss.focussnip.service.UserService;
import org.oss.focussnip.utils.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JWTRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTRealm.class);

    @Autowired
    private UserService userService;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法
     */
    @SneakyThrows
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = principals.toString();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 给该用户设置角色，角色信息存在 t_role 表中取
        authorizationInfo.setRoles(userService.getRoles(username));
        ObjectMapper om = new ObjectMapper();
        System.out.println(om.writeValueAsString(userService.getRoles(username)));
        // 给该用户设置权限，权限信息存在 t_permission 表中取
        authorizationInfo.setStringPermissions(userService.getPermissions(username));
        System.out.println(om.writeValueAsString(userService.getPermissions(username)));
        return authorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String jwtToken = (String) auth.getCredentials();

        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(jwtToken);
        if (StringUtils.isBlank(username)) {
            throw new AuthenticationException("Token is invalid!");
        }



        Users user = userService.getByUsername(username);
        if (user == null) {
            throw new AuthenticationException("User doesn't exist!");
        }

        if (!JWTUtil.verify(jwtToken, username)) {
            throw new AuthenticationException("token校验不通过");
        }

        return new SimpleAuthenticationInfo(username, jwtToken, "jwtRealm");
    }
}
