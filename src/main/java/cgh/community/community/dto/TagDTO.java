package cgh.community.community.dto;

import lombok.Data;

import java.util.List;

/**
 * 标签DTO
 * @author Akuma
 * @date 2020/5/11 10:09
 */
@Data
public class TagDTO {
    private String categoryName;    //标签分类名称
    private List<String> tags;  //该分类的标签列表
}
