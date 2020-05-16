package cgh.community.community.cache;

import cgh.community.community.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 热门标签缓存类
 * @author Akuma
 * @date 2020/5/16 20:03
 */
@Component
@Data
public class HotTagCache {
    private List<String> hots = null;   //最热门的max个标签

    /**
     * 更新热门标签
     * @param tags
     */
    public void updateTags(Map<String,Integer> tags){
        int max = 6;    //热门标签最大值为6
        //使用最小堆，根据标签的优先级计算得出最热门的max个标签
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>();

        tags.forEach((name,priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            if(priorityQueue.size() < max){
                priorityQueue.add(hotTagDTO);
            }
            else{
                HotTagDTO minHot = priorityQueue.peek();
                if(hotTagDTO.compareTo(minHot) > 0){
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTO);
                }
            }
        });
        List<String> sortedTags = new ArrayList<>();
        while(!priorityQueue.isEmpty()){
            sortedTags.add(0,priorityQueue.poll().getName());
        }
        hots = sortedTags;
        //System.out.println(hots);
    }
}
