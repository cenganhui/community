package cgh.community.community.enums;

/**
 * 通知状态枚举类
 * @author Akuma
 * @date 2020/5/12 20:33
 */
public enum NotificationStatusEnum {
    UNREAD(0),READ(1);

    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
