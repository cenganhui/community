package cgh.community.community.mapper;

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
}