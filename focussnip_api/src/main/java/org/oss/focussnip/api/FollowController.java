package org.oss.focussnip.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.model.Goods;
import org.oss.focussnip.model.Users;
import org.oss.focussnip.service.FollowService;
import org.oss.focussnip.utils.ListPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("关注模块API")
@RestController
@RequestMapping("follow")
@CrossOrigin(origins = "*")
public class FollowController {

    @Autowired
    private FollowService followService;

    @ApiOperation("查询用户关注列表")
    @GetMapping("/getStar")
    public BaseResponse getFollowedStars(@RequestParam("userId") int userId) {
        List<Users> stars = followService.getFollowStars(userId);
        return BaseResponse.getSuccessResponse(stars);
    }

    @ApiOperation("查看关注明星的活动")
    @GetMapping("/getFollowedGoods")
    public BaseResponse getFollowedGoods(@RequestParam("userId") int userId) {
        List<Goods> goods = followService.getFollowGoods(userId);
        ListPageUtil<Goods> listPageUtil = new ListPageUtil<>(goods, 10);
        return BaseResponse.getSuccessResponse(listPageUtil);
    }

    @ApiOperation("添加关注")
    @PostMapping("/insert")
    public BaseResponse followStar(@RequestParam("userId") int userId, @RequestParam("starId") int starId) {
        followService.followStar(userId, starId);
        return BaseResponse.getSuccessResponse(null);
    }

    @ApiOperation("取关")
    @DeleteMapping("/delete")
    public BaseResponse unfollowStar(@RequestParam("userId") int userId, @RequestParam("starId") int starId) {
        followService.unfollowStar(userId, starId);
        return BaseResponse.getSuccessResponse(null);
    }
}
