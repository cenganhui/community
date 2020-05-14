package cgh.community.community.mapper;

import cgh.community.community.dto.QuestionQueryDTO;
import cgh.community.community.model.Question;
import cgh.community.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 自定义扩展
 */
public interface QuestionExtMapper {

    /**
     * 根据问题id递增阅读数
     * @param question
     * @return
     */
    int incView(Question question);

    int incCommentCount(Question question);

    /**
     * 根据问题查询关联问题
     * @param question
     * @return
     */
    List<Question> selectRelated(Question question);

    /**
     * 根据搜索内容查询问题总数
     * @param questionQueryDTO
     * @return
     */
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    /**
     * 根据搜索问题查询问题列表
     * @param questionQueryDTO
     * @return
     */
    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}