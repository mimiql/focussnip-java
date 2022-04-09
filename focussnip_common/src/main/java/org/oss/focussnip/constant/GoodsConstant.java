package org.oss.focussnip.constant;

import io.swagger.models.auth.In;

public class GoodsConstant {
    /** 商品ID长度 */
    public static Integer GOODS_ID_LENGTH = 7;

    /** 商品上架状态 */
    public static Integer GOODS_STATUS_UP = 1;

    /** 商品下架状态 */
    public static Integer GOODS_STATUS_DOWN = 0;

    /** 商品类别
     * 0：包含以下四种
     * 1：演唱会
     * 2：综艺
     * 3：音乐剧
     * 4：其他 */
    public static Integer GOODS_CATEGORY_ALL = 0;
    public static Integer GOODS_CATEGORY_CONCERT = 1;
    public static Integer GOODS_CATEGORY_SHOW = 2;
    public static Integer GOODS_CATEGORY_MUSICAL = 3;
    public static Integer GOODS_CATEGORY_OTHER = 4;



}
