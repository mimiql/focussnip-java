package org.oss.focussnip.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Orders {
    @TableId(type = IdType.AUTO)
    private Long orderId;//主键
    private double price;
    private String description; // 订单描述信息
    private LocalDateTime createdTime;
    private LocalDateTime payedTime;
    private int status; // 订单状态
    private String username;
    private String goodsId;
}
