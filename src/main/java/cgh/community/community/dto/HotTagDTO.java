package cgh.community.community.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 热门标签DTO类
 * @author Akuma
 * @date 2020/5/16 20:23
 */
@Data
public class HotTagDTO implements Comparable{
    private String name;    //标签name
    private Integer priority;   //标签优先级

    /**
     * 重写比较方法
     * @param o
     * @return
     */
    @Override
    public int compareTo(@NotNull Object o) {
        return this.getPriority() - ((HotTagDTO)o).getPriority();
    }
}
