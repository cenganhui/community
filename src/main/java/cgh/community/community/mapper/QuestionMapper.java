package cgh.community.community.mapper;

import cgh.community.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 */
@Mapper
public interface QuestionMapper {

    /**
     * 插入一条问题
     * @param question
     */
    @Insert("INSERT INTO question (title,description,gmt_create,gmt_modified,creator,tag) VALUES(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);
}
