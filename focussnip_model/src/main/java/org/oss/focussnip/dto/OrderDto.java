package org.oss.focussnip.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class OrderDto {

    @Range(min = 0, max = 2, message = "订单状态范围是0-2")
    private int status; // 订单状态：0是未支付，1是进行中，2是已完成
}
