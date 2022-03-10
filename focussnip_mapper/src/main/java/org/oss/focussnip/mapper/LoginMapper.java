package org.oss.focussnip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.oss.focussnip.model.UserLogin;

@Mapper
public interface LoginMapper extends BaseMapper<UserLogin> {
}
