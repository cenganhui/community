package cgh.community.community.service;

import cgh.community.community.dto.PaginationDTO;
import cgh.community.community.dto.QuestionDTO;
import cgh.community.community.mapper.QuestionMapper;
import cgh.community.community.mapper.UserMapper;
import cgh.community.community.model.Question;
import cgh.community.community.model.User;
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
        Integer totalCount = questionMapper.count();    //获得问题总数
        paginationDTO.setPagination(totalCount,page,size);  //计算出分页问题DTO内的属性
        //如果当前页号<1 或者>总页数，则当前页号=1或者最后一页
        if(page < 1){
            page = 1;
        }
        if(page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }
        //计算页面偏移，根据偏移和分页数量查询出当前分页的问题list
        Integer offset = size * (page -1);
        List<Question> questionList = questionMapper.list(offset,size);
        //创建问题DTO list
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //循环遍历问题list，得到这个问题对象和对应的用户对象，注入进问题DTO中
        for (Question question : questionList) {
            //通过问题的创建用户id查询到用户对象
            User user = userMapper.findById(question.getCreator());
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
}
