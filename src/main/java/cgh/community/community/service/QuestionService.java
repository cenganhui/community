package cgh.community.community.service;

import cgh.community.community.dto.PaginationDTO;
import cgh.community.community.dto.QuestionDTO;
import cgh.community.community.mapper.QuestionMapper;
import cgh.community.community.mapper.UserMapper;
import cgh.community.community.model.Question;
import cgh.community.community.model.QuestionExample;
import cgh.community.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询所有问题及用户，返回分页问题DTO
     * @param page  当前页号
     * @param size  分页数量
     * @return
     */
    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();  //创建分页问题DTO
        Integer totalPage;
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());    //获得问题总数

        if(totalCount % size == 0){
            totalPage = totalCount / size;
        }
        else{
            totalPage = totalCount / size + 1;
        }
        //如果当前页号<1 或者>总页数，则当前页号=1或者最后一页
        if(page < 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);  //计算出分页问题DTO内的属性

        //计算页面偏移，根据偏移和分页数量查询出当前分页的问题list
        Integer offset = size * (page -1);

        List<Question> questionList = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(),new RowBounds(offset,size));

        //创建问题DTO list
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //循环遍历问题list，得到这个问题对象和对应的用户对象，注入进问题DTO中
        for (Question question : questionList) {
            //通过问题的创建用户id查询到用户对象
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            //创建一个问题DTO
            QuestionDTO questionDTO = new QuestionDTO();
            //工具类，将question对象中的属性复制到questionDTO对象中
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            //将问题DTO加入到问题DTO list中
            questionDTOList.add(questionDTO);
        }
        //将问题DTO list注入进分页问题DTO中
        paginationDTO.setQuestionList(questionDTOList);
        //返回分页问题DTO
        return paginationDTO;
    }

    /**
     * 根据用户id查询分页问题DTO（待）
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO listByUserId(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();  //创建分页问题DTO
        Integer totalPage;

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample); //获得问题总数

        if(totalCount % size == 0){
            totalPage = totalCount / size;
        }
        else{
            totalPage = totalCount / size + 1;
        }
        //如果当前页号<1 或者>总页数，则当前页号=1或者最后一页
        if(page < 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);  //计算出分页问题DTO内的属性

        //计算页面偏移，根据偏移和分页数量查询出当前分页的问题list
        Integer offset = size * (page -1);

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithBLOBsWithRowbounds(example,new RowBounds(offset,size));

        //创建问题DTO list
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //循环遍历问题list，得到这个问题对象和对应的用户对象，注入进问题DTO中
        for (Question question : questionList) {
            //通过问题的创建用户id查询到用户对象
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            //创建一个问题DTO
            QuestionDTO questionDTO = new QuestionDTO();
            //工具类，将question对象中的属性复制到questionDTO对象中
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            //将问题DTO加入到问题DTO list中
            questionDTOList.add(questionDTO);
        }
        //将问题DTO list注入进分页问题DTO中
        paginationDTO.setQuestionList(questionDTOList);
        //返回分页问题DTO
        return paginationDTO;
    }

    /**
     * 根据问题id获得问题DTO
     * @param id
     * @return
     */
    public QuestionDTO getById(Integer id) {
        //通过问题id查询到问题
        Question question = questionMapper.selectByPrimaryKey(id);
        //通过问题的创建用户id查询到用户对象
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        //创建一个问题DTO
        QuestionDTO questionDTO = new QuestionDTO();
        //工具类，将question对象中的属性复制到questionDTO对象中
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);

        return questionDTO;
    }

    /**
     * 通过问题来创建或更新问题
     * @param question
     */
    public void createOrUpdate(Question question) {
        if(question.getId() == null){   //问题为空则创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }
        else{   //否则更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());

            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(updateQuestion,new QuestionExample());
        }
    }
}
