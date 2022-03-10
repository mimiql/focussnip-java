package org.oss.focussnip;

import org.junit.jupiter.api.Test;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.exception.BusinessErrorException;
import org.oss.focussnip.exception.BusinessMsgEnum;
import org.oss.focussnip.mapper.LoginMapper;
import org.oss.focussnip.model.UserLogin;
import org.oss.focussnip.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@SpringBootTest
@RequestMapping("/test")
class FocussnipApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private LoginService loginService;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<UserLogin> userList = loginMapper.selectList(null);
        userList.forEach(System.out::println);
    }
}