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
    public List<Users> getFollowStars(int userId) {
        List<Integer> starsId = userMapper.getFollowStarId(userId);
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
    public List<Integer> getFollowStarsId(int userId) {
        return userMapper.getFollowStarId(userId);
    }

    @Override
    public Users getUserById(int userId) {
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.eq("id", userId);
        return userMapper.selectOne(qw);
    }

    @Override
    public List<Goods> getFollowGoods(int userId) {
        List<Integer> followStarId = userMapper.getFollowStarId(userId);
        List<Goods> goods = new ArrayList<>();
        for (Integer starId : followStarId) {
            List<Goods> goodsByStarId = goodsService.getGoodsByStarId(starId);
            goods.addAll(goodsByStarId);
        }
        return goods;
    }

    @Override
    public void followStar(int userId, int starId) {
        followMapper.insert(userId, starId);
    }

    @Override
    public void unfollowStar(int userId, int starId) {
        followMapper.delete(userId, starId);
    }


}
