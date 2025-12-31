package com.xummmerr.blog.mapper;

/**
 * @author HX
 * @version 1.0
 */
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xummmerr.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 * 继承 BaseMapper 后，自动拥有了 CRUD (增删改查) 能力
 */
@Mapper // 重要！加上这个注解，Spring Boot 才能扫描到它
public interface UserMapper extends BaseMapper<User> {
    // 这里我们可以什么都不写，MyBatis Plus 已经帮我们把活干完了
}
