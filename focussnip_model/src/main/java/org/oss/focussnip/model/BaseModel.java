package org.oss.focussnip.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseModel implements Serializable{
    @TableId(type = IdType.AUTO)
    protected Long id;

    @TableField(fill = FieldFill.INSERT)
    // 前端入参格式化
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // 后端到前端格式化
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime updatedTime;
}
