package org.oss.focussnip.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class permissions extends BaseModel {
    private String permissionsName;
    private Long roleId;
}
