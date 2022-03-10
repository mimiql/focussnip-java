package org.oss.focussnip.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oss.focussnip.exception.BusinessMsgEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class BaseResponse<T> {

    public static<T> BaseResponse<T> getSuccessResponse(T data){
        return new BaseResponse<T>(0,data);
    }

    public static<T> BaseResponse<T> getErrorResponse(String BusinessStatus, String BusinessMessage){
        return new BaseResponse<T>(1, BusinessStatus, BusinessMessage);
    }

    public static<T> BaseResponse<T> getErrorResponse(BusinessMsgEnum businessMsgEnum){
        return new BaseResponse<T>(1, businessMsgEnum);
    }

    /**
     * 仅可能为0或1
     * 0表示正常
     * 1表示出错
     */
    @JsonProperty
    private int status;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;

    private BaseResponse(int status,T data) {
        this.setStatus(status);
        this.setData(data);
    }

    private BaseResponse(int status,String BusinessStatus, String BusinessMessage) {
        this.setStatus(status);
        this.putExtra("code", BusinessStatus);
        this.putExtra("message", BusinessMessage);
    }

    private BaseResponse(int status,BusinessMsgEnum businessMsgEnum) {
        this.setStatus(status);
        this.putExtra("code", businessMsgEnum.code());
        this.putExtra("message", businessMsgEnum.msg());
    }

    private void setData(T data) {
        this.data = data;
    }

    private void setStatus(int status) {
        this.status = status;
    }

    @Data
    public class InnerBase {
        private String message;
        private Integer status;
        private Map<String, String> extra;

        public void setStatus(HttpStatus status) {
            this.setStatus(status.value());
        }

        public void setStatus(Integer status) {
            this.status = status;
            RequestAttributes request = RequestContextHolder.getRequestAttributes();
            if (request != null) {
                request.setAttribute("httpStatus", this.getStatus(), RequestAttributes.SCOPE_REQUEST);
            }
        }

        public InnerBase() {
            this.setMessage(HttpStatus.OK.getReasonPhrase());
            this.setStatus(HttpStatus.OK);
            this.setExtra(new HashMap<>());
        }
    }

    @JsonProperty
    //@ApiModelProperty(hidden = true) // 暂时不显示这个字段, 看前端能不能在拦截器里处理
    private InnerBase base = new InnerBase();

    public void setHttpMessage(String message) {
        base.setMessage(message);
    }

    public void setHttpStatus(HttpStatus status) {
        base.setStatus(status);
        base.setMessage(status.getReasonPhrase());
    }

    public void putExtra(String key, String value) {
        base.getExtra().put(key, value);
    }
}