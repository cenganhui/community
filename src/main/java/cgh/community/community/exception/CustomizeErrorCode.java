package cgh.community.community.exception;

/**
 * 异常信息枚举类
 * @author Akuma
 * @date 2020/5/4 15:51
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"问题不存在"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"未登录"),
    SYS_ERROR(2004,"稍等下吧"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或评论不存在"),
    COMMENT_NOT_FOUND(2006,"评论不存在"),
    ;

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
