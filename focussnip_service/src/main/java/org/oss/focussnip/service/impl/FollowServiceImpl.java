package org.oss.focussnip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.oss.focussnip.mapper.FollowMapper;
import org.oss.focussnip.mapper.UserMapper;
import org.oss.focussnip.model.Goods;
import org.oss.focussnip.model.Users;
import org.oss.focussnip.service.FollowService;
import org.oss.focussnip.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private GoodsService goodsService;

    @Override
    public List<Users> getFollowStars(String username) {
        List<Integer> starsId = userMapper.getFollowStarId(username);
        starsId.removeAll(Collections.singleton(null));
        if (starsId.isEmpty()) {
            return null;
        }
        List<Users> stars = new ArrayList<>();

        for (Integer star : starsId) {
            QueryWrapper<Users> qw = new QueryWrapper<>();
            qw.eq("id", star);
            Users user = userMapper.selectOne(qw);
            stars.add(user);
        }
        return stars;
    }

    @Override
    public List<Integer> getFollowStarsId(String username) {
        return userMapper.getFollowStarId(username);
    }

    @Override
    public Users getUserById(String username) {
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.eq("username", username);
        return userMapper.selectOne(qw);
    }

    @Override
    public List<Goods> getFollowGoods(String username) {
        // 如果查询不到，会返回一个元素为null的list
        List<Integer> followStarId = userMapper.getFollowStarId(username);
        followStarId.removeAll(Collections.singleton(null)); // 移除集合中所有的null值
        if (followStarId.isEmpty()) {
            return null;
        }
        List<Goods> goods = new ArrayList<>();
        for (Integer starId : followStarId) {
            List<Goods> goodsByStarId = goodsService.getGoodsByStarId(starId);
            goods.addAll(goodsByStarId);
        }
        return goods;
    }

    @Override
    public void followStar(String username, int starId) {
        followMapper.insert(username, starId);
    }

    @Override
    public void unfollowStar(String username, int starId) {
        followMapper.delete(username, starId);
    }

}
