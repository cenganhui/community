package cgh.community.community.mapper;

import cgh.community.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 查询所有问题
     * @return
     */
    @Select("SELECT * FROM question")
    List<Question> list();
}
