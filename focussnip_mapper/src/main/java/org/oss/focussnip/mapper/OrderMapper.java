package org.oss.focussnip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.oss.focussnip.model.Orders;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
