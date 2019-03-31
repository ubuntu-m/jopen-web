package io.jopen.web.core.model;

/**
 * response model
 */
public class ResponseModel {
    int code;
    String message;
    boolean success = false;
    private Object data;


    public ResponseModel(ErrorEnum error) {
        code = error.getCode();
        message = error.getMessage();
    }

    public ResponseModel(ErrorEnum error, Throwable ex) {
        code = error.getCode();
        message = error.getMessage().concat(",").concat(ex.getMessage());
    }

    public ResponseModel(ErrorEnum error, Object data, boolean success) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.data = data;
        this.success = success;
    }

    public static ResponseModel ok(Object data) {
        return new ResponseModel(ErrorEnum.REQUEST_OK, data, true);
    }

    public static ResponseModel ok() {
        return new ResponseModel(ErrorEnum.REQUEST_OK, null, true);
    }

    public static ResponseModel error(ErrorEnum error) {
        return new ResponseModel(error);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
