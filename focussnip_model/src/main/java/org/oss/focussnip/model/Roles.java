package org.oss.focussnip.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Roles extends BaseModel{
    private String roleName;
}
