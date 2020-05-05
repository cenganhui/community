package cgh.community.community.enums;

/**
 * 评论类型枚举类
 * @author Akuma
 * @date 2020/5/5 15:19
 */
public enum CommentTypeEnum {
    QUESTION(1),    //type=1代表评论的是问题
    COMMENT(2);     //type=2代表评论的是评论
    private Integer type;

    CommentTypeEnum(Integer type){
        this.type = type;
    }

    /**
     * 判断评论类型是否存在
     * @param type
     * @return
     */
    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if(commentTypeEnum.getType() == type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }
}
