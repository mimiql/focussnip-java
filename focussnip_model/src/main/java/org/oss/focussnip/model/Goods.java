package org.oss.focussnip.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Goods {
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


}
