package com.ismyself.user.dao;
import com.ismyself.user.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author:txw
 * @Description:User的Dao
 */
public interface UserMapper extends Mapper<User> {

    /**
     * 给用户添加积分
     * @param username
     * @param points
     */
    @Update("update tb_user set points = points + #{points} where username = #{username}")
    int addUserPoints(@Param("username") String username,@Param("points") Integer points);
}
