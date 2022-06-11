package org.oss.focussnip.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class SnapGoods extends BaseModel{
    private String goodsName;
    private String address;
    private String picture;
    private int stock;
    // 前端入参格式化
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    // 后端到前端格式化
    @JsonSerialize(using= ToStringSerializer.class)
    private BigDecimal price;
    private String description;
    private String tip;
    // 前端入参格式化
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // 后端到前端格式化
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime marketTime;
    // 前端入参格式化
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // 后端到前端格式化
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endTime;
    // 前端入参格式化
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // 后端到前端格式化
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime holdTime;
    private int category;
    private int starId;
    private int status;
    private String detail;
}
