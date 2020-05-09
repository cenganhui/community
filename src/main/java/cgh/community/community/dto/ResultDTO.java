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
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public ResultDTO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回错误结果DTO
     * @param customizeErrorCode
     * @return
     */
    public static ResultDTO errorOf(CustomizeErrorCode customizeErrorCode) {
        return new ResultDTO(customizeErrorCode.getCode(),customizeErrorCode.getMessage(),null);
    }

    /**
     * 返回错误结果DTO
     * @param e
     * @return
     */
    public static ResultDTO errorOf(CustomizeException e) {
        return new ResultDTO(e.getCode(),e.getMessage(),null);
    }

    /**
     * 返回成功结果DTO
     * @return
     */
    public static ResultDTO okOf() {
        return new ResultDTO(200,"ok",null);
    }

    /**
     * 返回成功结果DTO
     * @param t
     * @param <T>
     * @return
     */
    public static <T> ResultDTO okOf(T t) {
        return new ResultDTO(200,"ok",t);
    }

}
