package org.oss.focussnip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowMapper extends BaseMapper {
    @Insert("INSERT into user_star (username, star_id) values (#{username}, #{starId})")
    void insert(String username, int starId); // 插入 用户-明星 关系

    @Delete("DELETE from user_star where username=#{username} and star_id=#{starId}")
    void delete(String username, int starId);
}
