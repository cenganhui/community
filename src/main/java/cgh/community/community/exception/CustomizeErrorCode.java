package cgh.community.community.exception;

/**
 * 异常信息枚举类
 * @author Akuma
 * @date 2020/5/4 15:51
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND("问题不存在");

    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
