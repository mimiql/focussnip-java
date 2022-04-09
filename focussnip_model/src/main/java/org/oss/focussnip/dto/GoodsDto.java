package org.oss.focussnip.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class GoodsDto {
    private static final String REGEXP = "^(((20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30)))" +
            "\\s(20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$";

    private String goodsName = "";

    private String address = "";

    private int stock = 0;

    private String description = "";

    @Range(min = 0, max = 4, message = "范围0-4")
    private int category = 0;

    private int starId = -1;

    private int status = 1;

    @Data
    public static class GoodsAddDto extends GoodsDto{
        private String picture;

        private double price;

        private String tip;

        @Pattern(regexp = REGEXP, message = "日期格式yyyy-MM-dd HH:mm:ss")
        private String marketTimeStr = "2020-01-01 00:00:00";

        @Pattern(regexp = REGEXP, message = "日期格式yyyy-MM-dd HH:mm:ss")
        private String holeTimeStr = "2020-01-01 00:00:00";
    }

    @Data
    public static class GoodsUpdateDto extends GoodsAddDto{
        @NotBlank(message = "goodsId不能为空")
        private String goodsId;
    }

    @Data
    public static class GoodsQueryDto extends GoodsDto{
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


}
