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

    private int stock = -1;

    private String description = "";

    @Range(min = 0, max = 4, message = "范围0-4")
    private int category = 0;

    private int starId = -1;

    private int status = 1;


}
