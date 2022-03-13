package org.oss.focussnip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.oss.focussnip.model.Users;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<Users> {

    @Select("SELECT r.role_name " +
            "FROM users AS u " +
            "LEFT JOIN roles AS r ON u.role_id = r.id " +
            "WHERE u.username = #{username}")
    List<String> getRoles(String username);

    @Select("SELECT p.permission_name FROM users AS u " +
            "LEFT JOIN roles AS r ON u.role_id = r.id " +
            "LEFT JOIN permissions as p ON p.role_id = r.id " +
            "WHERE u.username = #{username}")
    List<String>getPermissions(String username);
}
