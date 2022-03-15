package org.oss.focussnip.exception;

public enum BusinessMsgEnum {
    /** 参数异常 */
    PARMETER_EXCEPTION("101", "参数异常!"),
    /** 参数过大 */
    PARMETER_BIG_EXCEPTION("102", "参数过大"),
    /** 等待超时 */
    SERVICE_TIME_OUT("103", "服务调用超时！"),
    /** 用户名重复异常 */
    USER_EXIST_EXCEPTION("104", "用户名重复异常!"),
    /** 用户名不存在 */
    USER_NOT_EXIST_EXCEPTION("105", "用户名不存在!"),
    /** 用户名不存在 */
    USER_LOGIN_EXCEPTION("106", "用户名或密码错误!"),
    /** 请求方式不支持 */
    REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION("302", "请求方式不支持!"),
    /** 权限异常 */
    Authentication_EXCEPTION("401", "账号权限不足"),
    Authorization_EXCEPTION("403", "角色权限不足"),
    /** 500 : 发生异常 */
    UNEXPECTED_EXCEPTION("500", "系统发生异常，请联系管理员！"),
    /** 666 : 测试用异常 */
    TEST_EXCEPTION("666", "测试用异常");

    /**
     * 消息码
     */
    private String code;
    /**
     * 消息内容
     */
    private String msg;

    private BusinessMsgEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
