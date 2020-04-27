package cgh.community.community.mapper;

import cgh.community.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
public interface UserMapper {
    /**
     * 插入一条用户信息
     * @param user
     */
    @Insert("INSERT INTO user (name,account_id,token,gmt_create,gmt_modified,avatar_url) VALUES (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    @Select("SELECT * FROM user WHERE token=#{token}")
    User findByToken(@Param("token") String token);

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @Select("SELECT * FROM user WHERE id=#{id}")
    User findById(@Param("id") Integer id);
}
