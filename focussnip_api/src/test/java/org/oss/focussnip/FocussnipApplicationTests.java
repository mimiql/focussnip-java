package org.oss.focussnip;

import org.junit.jupiter.api.Test;
import org.oss.focussnip.mapper.UserMapper;
import org.oss.focussnip.model.Users;
import org.oss.focussnip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@SpringBootTest
@RequestMapping("/test")
class FocussnipApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private UserMapper loginMapper;
    @Autowired
    private UserService loginService;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<Users> userList = loginMapper.selectList(null);
        userList.forEach(System.out::println);
    }
}