package cgh.community.community.cache;

import cgh.community.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签缓存
 * @author Akuma
 * @date 2020/5/11 10:09
 */
public class TagCache {
    /**
     * 获取所有标签
     * @return
     */
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOList = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("javascript","php","css","html","html5","java","node.js","python","c++","c","golang","objective-c","typescript","shell","swift","#c","sass","ruby","bash","less","asp.net","lua","scala","coffeescript","actionscript","rust","erlang","perl"));
        tagDTOList.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("laravel","spring","express","django","flask","yii","ruby-on-rails","tornado","koa","struts"));
        tagDTOList.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux","nginx","docker","apache","ubuntu","centos","缓存","tomcat","负载均衡","unix","hadoop","windows-server"));
        tagDTOList.add(server);

        TagDTO database = new TagDTO();
        database.setCategoryName("数据库和缓存");
        database.setTags(Arrays.asList("mysql","redis","mongodb","sql","oracle","nosql","memcached","sqlserver","postgresql","sqlite"));
        tagDTOList.add(database);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git","github","visual-studio-code","vim","sublime-text","xcode","intellij-idea","eclipse","maven","ide","svn","visual-studio","atom","emacs","textmate","hg"));
        tagDTOList.add(tool);

        TagDTO os = new TagDTO();
        os.setCategoryName("系统设备");
        os.setTags(Arrays.asList("android","ios","chrome","windows","iphone","firefox","internet-explorer","safari","ipad","opera","apple-watch"));
        tagDTOList.add(os);

        TagDTO other = new TagDTO();
        other.setCategoryName("其他");
        other.setTags(Arrays.asList("html5","react.js","搜索引擎","virtualenv","lucene"));
        tagDTOList.add(other);

        return tagDTOList;
    }

    /**
     * 标签验证
     * @param tags
     * @return  返回非法的标签
     */
    public static String filterInvalid(String tags){
        String[] split = StringUtils.split(tags,",");
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
