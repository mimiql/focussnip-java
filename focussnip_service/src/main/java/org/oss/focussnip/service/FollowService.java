package org.oss.focussnip.service;

import org.oss.focussnip.model.Goods;
import org.oss.focussnip.model.Users;

import java.util.List;

public interface FollowService {
    List<Users> getFollowStars(int userId);

    List<Integer> getFollowStarsId(int userId);

    Users getUserById(int userId);

    List<Goods> getFollowGoods(int userId);

    void followStar(int userId, int starId);

    void unfollowStar(int userId, int starId);//取关
}
