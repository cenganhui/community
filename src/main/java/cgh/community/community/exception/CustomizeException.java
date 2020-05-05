package cgh.community.community.exception;

/**
 * 自定义异常类
 * @author Akuma
 * @date 2020/5/4 15:39
 */
public class CustomizeException extends RuntimeException{
    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode(){
        return code;
    }
}
