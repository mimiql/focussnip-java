package org.oss.focussnip.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class BaseModel implements Serializable{
    protected Long id;
    private Timestamp createdTime;
    private Timestamp updatedTime;
}
