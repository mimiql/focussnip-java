package org.oss.focussnip.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Orders {
    private int orderId;//主键
    private double price;
    private String description; // 订单描述信息
    private LocalDateTime createdTime;
    private LocalDateTime payedTime;
    private int status; // 订单状态
    private int userId;
}
