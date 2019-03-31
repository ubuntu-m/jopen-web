package io.jopen.web.core.model;

/**
 * 错误码
 */
public enum ErrorEnum {
    NO_APPKEY(350, "缺少appKey头部"),
    NO_SIGNATURE(351, "缺少signature"),
    NO_TIMESTAMP(352, "缺少timestamp"),
    INVALID_TIMESTAMP(353, "无效时间戳"),
    TIMESTAMP_TIMEOUT(354, "链接过期"),
    INVALID_SIGNATURE(355, "无效签名"),
    NO_AUTH(356, "鉴权失败(无权限)"),
    NO_SERVICE(357, "鉴权失败(无此接口)"),
    NOAVAILABLE_SERVICE(358, "后端无存活服务"),
    BACKEND_CONNREFUSED(379, "后端服务拒绝连接"),
    BACKEND_COMEXCEPTION(380, "网关与后端通讯异常"),
    BACKEND_TIMEOUT(381, "后端服务超时"),
    BACKEND_4XX_5XX(382, "后端http异常(4XX or 5xx status code)"),
    FREQUENT_REQUESTS(383, "请不要频繁请求"),
    OTHER(384, "系统其他错误"),
    REQUEST_OK(385, "OK");

    private int code;
    private String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
