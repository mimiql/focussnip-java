package org.oss.focussnip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowMapper extends BaseMapper {
    @Insert("INSERT into user_star (user_id, star_id) values (#{userId}, #{starId})")
    void insert(int userId, int starId); // 插入 用户-明星 关系

    @Delete("DELETE from user_star where user_id=#{userId} and star_id=#{starId}")
    void delete(int userId, int starId);
}
