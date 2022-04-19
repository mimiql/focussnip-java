package org.oss.focussnip.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class GoodsQueryDto extends GoodsDto{
    private static final String REGEXP = "^(((20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30)))" +
            "\\s(20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$";

    private String goodsId;

    private double priceStart = 0;

    private double priceEnd = 1000000;

    @Pattern(regexp = REGEXP, message = "日期格式yyyy-MM-dd HH:mm:ss")
    private String marketTimeStartStr = "2020-01-01 00:00:00";

    @Pattern(regexp = REGEXP, message = "日期格式yyyy-MM-dd HH:mm:ss")
    private String marketTimeEndStr = "2030-01-01 00:00:00";

    @Pattern(regexp = REGEXP, message = "日期格式yyyy-MM-dd HH:mm:ss")
    private String holeTimeStartStr = "2020-01-01 00:00:00";

    @Pattern(regexp = REGEXP, message = "日期格式yyyy-MM-dd HH:mm:ss")
    private String holeTimeEndStr = "2030-01-01 00:00:00";

    private int pageNum = 0;

    private int pageSize = 30;
}
