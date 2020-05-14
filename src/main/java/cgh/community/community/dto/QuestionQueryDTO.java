package cgh.community.community.dto;

import lombok.Data;

/**
 * 问题查询DTO
 * @author Akuma
 * @date 2020/5/14 15:26
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
}
