package cgh.community.community.service;

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

    public List<QuestionDTO> list() {
        List<Question> questionList = questionMapper.list();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            //System.out.println("creatorId:"+question.getCreator());
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //工具类，将question对象中的属性复制到questionDTO对象中
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            //System.out.println("user:"+user.toString());
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
