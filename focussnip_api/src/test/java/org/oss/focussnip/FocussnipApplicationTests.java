package org.oss.focussnip;

import org.junit.jupiter.api.Test;
import org.oss.focussnip.model.SnapGoods;
import org.oss.focussnip.model.Users;
import org.oss.focussnip.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@SpringBootTest
@RequestMapping("/test")
class FocussnipApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private RedisUtil<String, SnapGoods> redisUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testSelect() {
        System.out.println(redisUtil.getValue("snap."+4020+".stock"));
        System.out.println(redisUtil.incr("snap."+4020+".stock"));
    }
}