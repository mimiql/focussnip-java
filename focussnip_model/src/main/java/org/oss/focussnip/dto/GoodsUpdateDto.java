package org.oss.focussnip.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GoodsUpdateDto extends GoodsAddDto {
    @NotBlank(message = "goodsId不能为空")
    private String goodsId;
}
