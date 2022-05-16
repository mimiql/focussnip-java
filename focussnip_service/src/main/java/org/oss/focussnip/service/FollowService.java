package org.oss.focussnip.service;

import org.oss.focussnip.model.Goods;
import org.oss.focussnip.model.Users;

import java.util.List;

public interface FollowService {
    List<Users> getFollowStars(String username);

    List<Integer> getFollowStarsId(String username);

    Users getUserById(String username);

    List<Goods> getFollowGoods(String username);

    void followStar(String username, int starId);

    void unfollowStar(String username, int starId);//取关
}
