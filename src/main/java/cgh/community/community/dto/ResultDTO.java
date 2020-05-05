package cgh.community.community.dto;

import cgh.community.community.exception.CustomizeErrorCode;
import cgh.community.community.exception.CustomizeException;
import lombok.Data;

/**
 * 返回结果DTO类
 * @author Akuma
 * @date 2020/5/5 15:09
 */
@Data
public class ResultDTO {
    private Integer code;
    private String message;

    public ResultDTO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 返回错误结果DTO
     * @param customizeErrorCode
     * @return
     */
    public static ResultDTO errorOf(CustomizeErrorCode customizeErrorCode) {
        return new ResultDTO(customizeErrorCode.getCode(),customizeErrorCode.getMessage());
    }

    /**
     * 返回错误结果DTO
     * @param e
     * @return
     */
    public static ResultDTO errorOf(CustomizeException e) {
        return new ResultDTO(e.getCode(),e.getMessage());
    }

    /**
     * 返回成功结果DTO
     * @return
     */
    public static ResultDTO okOf() {
        return new ResultDTO(200,"ok");
    }


}
