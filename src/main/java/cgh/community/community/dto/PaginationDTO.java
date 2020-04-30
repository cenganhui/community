package cgh.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页问题DTO
 * @author Akuma
 * @date 2020/4/30 12:24
 */
@Data
public class PaginationDTO {
    private List<QuestionDTO> questionList; //问题list
    private boolean showPrevious;   //是否显示前一页
    private boolean showFirstPage;  //是否显示第一页
    private boolean showNext;   //是否显示下一页
    private boolean showEndPage;    //是否显示最后一页
    private Integer page;   //当前页号
    private Integer totalPage;  //总页数
    private List<Integer> pages = new ArrayList<>();    //页号list

    /**
     * 计算出各个属性
     * @param totalCount    总问题数
     * @param page  当前页号
     * @param size  分页数量
     */
    public void setPagination(Integer totalCount, Integer page, Integer size) {
        //根据总问题数和分页数量计算出总页数
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
        //设置当前页号
        this.page = page;
        //将当前页号加入到页号list
        pages.add(page);
        //通过循环，保持当前页前面有3个页面（足够的话），后面有3个页面（足够的话）
        for(int i=1;i<=3;i++){
            if(page - i > 0){
                pages.add(0,page - i);
            }
            if(page + i <= totalPage){
                pages.add(page + i);
            }
        }
        //是否展示上一页
        if(page == 1){
            showPrevious = false;
        }
        else{
            showPrevious = true;
        }
        //是否展示下一页
        if(page == totalPage){
            showNext =false;
        }
        else{
            showNext = true;
        }
        //是否展示第一页
        if(pages.contains(1)){
            showFirstPage = false;
        }
        else{
            showFirstPage = true;
        }
        //是否展示最后一页
        if(pages.contains(totalPage)){
            showEndPage = false;
        }
        else{
            showEndPage = true;
        }
    }
}
