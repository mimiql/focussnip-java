package org.oss.focussnip.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.web.util.WebUtils;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.exception.BusinessMsgEnum;
import org.oss.focussnip.utils.Config;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class JWTFilter extends BasicHttpAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTFilter.class);

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 免登录接口判定
        if(super.isAccessAllowed(request,response,mappedValue)){
            return true;
        }

        if (isLoginAttempt(request, response)) {
            try {
                return this.executeLogin(request, response);
            } catch (Exception e) {
                LOGGER.warn("Wrong Token",e);
                return false;
            }
        }
        return false;
    }

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(Config.JWT_CUSTOMER_TOKEN_NAME);
        return authorization != null;
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(Config.JWT_CUSTOMER_TOKEN_NAME);

        JWTToken token = new JWTToken(authorization);
        try {
            // 提交给realm进行登入，如果错误他会抛出异常并被捕获
            getSubject(request, response).login(token);
            // 如果没有抛出异常则代表登入成功，返回true
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        LOGGER.debug("Authentication required: sending 401 Authentication challenge response.");
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
//        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setCharacterEncoding("utf-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        final String message = "未认证，请在前端系统进行认证";
        final Integer status = 401;
        try (PrintWriter out = httpResponse.getWriter()) {
//            String responseJson = "{\"message\":\"" + message + "\"}";
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(BaseResponse.getErrorResponse(BusinessMsgEnum.Authentication_EXCEPTION));
            out.print(json);
        } catch (IOException e) {
            LOGGER.error("sendChallenge error：", e);
        }
        return false;
    }

}
