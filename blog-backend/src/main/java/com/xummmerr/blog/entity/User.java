package com.xummmerr.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库中的 sys_user 表
 */
@Data // Lombok注解：自动生成Getter, Setter, toString等方法，不用手写了
@TableName("sys_user") // 告诉MyBatis Plus：这个类对应数据库里的 sys_user 表
public class User {

    @TableId(type = IdType.AUTO) // 告诉MP：这是主键，且是自增的
    private Long id;

    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String email;
    private LocalDateTime createTime;
}