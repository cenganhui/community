package cgh.community.community.service;

import cgh.community.community.dto.PaginationDTO;
import cgh.community.community.dto.QuestionDTO;
import cgh.community.community.dto.QuestionQueryDTO;
import cgh.community.community.exception.CustomizeErrorCode;
import cgh.community.community.exception.CustomizeException;
import cgh.community.community.mapper.QuestionExtMapper;
import cgh.community.community.mapper.QuestionMapper;
import cgh.community.community.mapper.UserMapper;
import cgh.community.community.model.Question;
import cgh.community.community.model.QuestionExample;
import cgh.community.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据搜索内容和标签查询所有问题及用户，返回分页问题DTO
     * @param search    搜索内容
     * @param tag   标签
     * @param page  当前页号
     * @param size  分页数量
     * @return
     */
    public PaginationDTO list(String search, String tag, Integer page, Integer size) {
        //分割搜索字符串
        if(StringUtils.isNoneBlank(search)){

            String[] tags = StringUtils.split(search," ");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();  //创建分页内容DTO
        Integer totalPage;
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setTag(tag);

        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);    //获得问题总数

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
        Integer offset = page < 1 ? 0 : size * (page -1);

        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        List<Question> questionList = questionExtMapper.selectBySearch(questionQueryDTO);

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
            //questionDTO.cutTag();
            //将问题DTO加入到问题DTO list中
            questionDTOList.add(questionDTO);
        }
        //将问题DTO list注入进分页内容DTO中
        paginationDTO.setData(questionDTOList);
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
    public PaginationDTO listByUserId(Long userId, Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();  //创建分页内容DTO
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
        example.setOrderByClause("gmt_create desc");
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
            //questionDTO.cutTag();
            //将问题DTO加入到问题DTO list中
            questionDTOList.add(questionDTO);
        }
        //将问题DTO list注入进分页内容DTO中
        paginationDTO.setData(questionDTOList);
        //返回分页问题DTO
        return paginationDTO;
    }

    /**
     * 根据问题id获得问题DTO
     * @param id
     * @return
     */
    public QuestionDTO getById(Long id) {
        //通过问题id查询到问题
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        //通过问题的创建用户id查询到用户对象
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        //创建一个问题DTO
        QuestionDTO questionDTO = new QuestionDTO();
        //工具类，将question对象中的属性复制到questionDTO对象中
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        //分割标签
        questionDTO.cutTag();
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
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
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
            int updated = questionMapper.updateByExampleSelective(updateQuestion,questionExample);
            if(updated != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    /**
     * 根据问题id递增问题的阅读数
     * @param id
     */
    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        else{
            String tag = queryDTO.getTag().replace(",","|");
            Question question = new Question();
            question.setId(queryDTO.getId());
            question.setTag(tag);
            List<Question> questionList = questionExtMapper.selectRelated(question);
            List<QuestionDTO> questionDTOList = questionList.stream().map(q -> {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(q,questionDTO);
                return questionDTO;
            }).collect(Collectors.toList());

            return questionDTOList;
        }
    }
}
