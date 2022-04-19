package org.oss.focussnip.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;

@Data
public class GoodsAddDto extends GoodsDto {
    private static final String REGEXP = "^(((20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30)))" +
            "\\s(20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$";

    private String picture;

    private double price;

    private String tip;

    @Pattern(regexp = REGEXP, message = "日期格式yyyy-MM-dd HH:mm:ss")
    private String marketTimeStr = "2020-01-01 00:00:00";

    @Pattern(regexp = REGEXP, message = "日期格式yyyy-MM-dd HH:mm:ss")
    private String holeTimeStr = "2020-01-01 00:00:00";
}
