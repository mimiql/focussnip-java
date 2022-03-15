package org.oss.focussnip.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oss.focussnip.exception.BusinessMsgEnum;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class BaseResponse<T> {

    public static<T> BaseResponse<T> getSuccessResponse(T data){
        return new BaseResponse<T>(0,data);
    }

    public static<T> BaseResponse<T> getErrorResponse(String businessStatus, String businessMessage){
        return new BaseResponse<T>(1, businessStatus, businessMessage);
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

    @JsonProperty
    private String message;

    @JsonIgnore
    public static String REDIRECT = "redirect";

    private BaseResponse(int status,T data) {
        this.setStatus(status);
        this.setData(data);
        this.setMessage(HttpStatus.OK.getReasonPhrase());
    }

    private BaseResponse(int status,String businessStatus, String businessMessage) {
        this.setStatus(status);
        this.setMessage(businessMessage);
        this.setBusinessInfo(businessStatus, businessMessage);
    }

    private BaseResponse(int status,BusinessMsgEnum businessMsgEnum) {
        this.setStatus(status);
        this.setMessage(businessMsgEnum.msg());
        this.setBusinessInfo(businessMsgEnum);
    }

    private void setData(T data) {
        this.data = data;
    }

    private void setStatus(int status) {
        this.status = status;
    }

    private void setMessage(String message){
        this.message = message;
    }

    public BaseResponse<T> setRedirect(String url){
        this.putExtra(REDIRECT,url);
        return this;
    }

    @Data
    public class InnerBase {
        private String message;
        private String code;
        private Map<String, String> extra;

        public void setCode(HttpStatus status) {
            this.setCode(String.valueOf(status.value()));
        }

        public void setCode(String code) {
            this.code = code;
        }

        public InnerBase() {
            this.setMessage(HttpStatus.OK.getReasonPhrase());
            this.setCode(HttpStatus.OK);
            this.setExtra(new HashMap<>());
        }
    }

    @JsonProperty
    //@ApiModelProperty(hidden = true) // 暂时不显示这个字段, 看前端能不能在拦截器里处理
    private InnerBase base = new InnerBase();

    private void setBusinessInfo(BusinessMsgEnum businessMsg) {
        base.setCode(businessMsg.code());
        base.setMessage(businessMsg.msg());
    }

    private void setBusinessInfo(String code, String msg) {
        base.setCode(code);
        base.setMessage(msg);
    }

    private void putExtra(String key, String value) {
        base.getExtra().put(key, value);
    }
}