package cgh.community.community.mapper;

import cgh.community.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
     * @param offset
     * @param size
     */
    @Select("SELECT * FROM question LIMIT #{offset},#{size}")
    List<Question> list(@Param("offset") Integer offset,@Param("size") Integer size);

    /**
     * 查询所有问题的总数
     * @return
     */
    @Select("SELECT COUNT(1) FROM question")
    Integer count();

    /**
     * 根据用户id查询问题列表
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    @Select("SELECT * FROM question WHERE creator=#{userId} LIMIT #{offset},#{size}")
    List<Question> listByUserId(@Param("userId") Integer userId,@Param("offset") Integer offset,@Param("size") Integer size);

    /**
     * 根据用户id查询问题总数
     * @param userId
     * @return
     */
    @Select("SELECT COUNT(1) FROM question WHERE creator=#{userId}")
    Integer countByUserId(@Param("userId") Integer userId);
}
