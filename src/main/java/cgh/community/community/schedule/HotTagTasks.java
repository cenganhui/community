package cgh.community.community.schedule;

import cgh.community.community.cache.HotTagCache;
import cgh.community.community.mapper.QuestionMapper;
import cgh.community.community.model.Question;
import cgh.community.community.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 定时器，用于更新热门标签
 * @author Akuma
 * @date 2020/5/16 19:32
 */
@Component
@Slf4j
public class HotTagTasks {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;

    /**
     * 定时更新热门标签
     */
    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)  //单位是毫秒，1000 * 60 * 60 * 3 => 3小时执行一次
    public void hotTagSchedule() {
        int offset = 0;
        int limit = 5;
        log.info("hotTagSchedule start {}", new Date());
        List<Question> list = new ArrayList<>();
        Map<String,Integer> priorities = new HashMap<>();
        //分页统计计算标签优先级
        while(offset == 0 || list.size() == limit){
            list = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(),new RowBounds(offset,limit));
            for (Question question : list) {
                String[] tags = StringUtils.split(question.getTag(),",");
                //一个问题中的每个标签的优先级 = 5 + 评论数
                //每个标签的优先级 += 每个问题中的这个标签的优先级
                for (String tag : tags) {
                    Integer priority = priorities.get(tag);
                    if(priority != null){
                        priorities.put(tag,priority + 5 + question.getCommentCount());
                    }
                    else{
                        priorities.put(tag,5 + question.getCommentCount());
                    }
                }

            }
            offset += limit;
        }
//        priorities.forEach(
//                (k,v) -> {
//                    System.out.println(k + ":" + v);
//                }
//        );
        //更新热门标签
        hotTagCache.updateTags(priorities);
        log.info("hotTagSchedule stop {}",new Date());
    }
}
