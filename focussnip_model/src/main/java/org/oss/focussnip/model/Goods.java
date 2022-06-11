package org.oss.focussnip.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Goods {
    @TableId(type = IdType.AUTO)
    private String goodsId;
    private String goodsName;
    private String address;
    private String picture;
    private int stock;
    private double price;
    private String description;
    private String tip;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime marketTime;
    private LocalDateTime holdTime;
    private int category;
    private int starId;
    private int status;
    private String detail;

}
