package com.xummmerr.blog.controller;

import com.xummmerr.blog.entity.User;
import com.xummmerr.blog.mapper.UserMapper;
import com.xummmerr.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 告诉Spring：我是Web接口，返回的数据是JSON
@RequestMapping("/user") // 这个类的所有接口都以 /user 开头
public class UserController {

    // 注入我们刚才写的 Mapper
    @Autowired
    private UserMapper userMapper;

    // 浏览器访问：http://localhost:8080/user/list
    @GetMapping("/list")
    public List<User> list() {
        // selectList(null) 意味着查询所有数据，没有条件
        return userMapper.selectList(null);
    }

    // 3. 登录接口 (POST)
    // 这里的 @RequestBody User user 表示接收前端传来的 {username: "...", password: "..."}
    @PostMapping("/login")
    public Result login(@RequestBody User loginUser) {
        // 1. 拿着用户名去数据库查，看看有没有这个人
        // QueryWrapper 是 MyBatis Plus 提供的“查询条件构造器”
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("username", loginUser.getUsername());
        queryWrapper.eq("password", loginUser.getPassword());

        // getOne 表示只查一条数据
        User user = userMapper.selectOne(queryWrapper);

        // 2. 判断结果
        if (user == null) {
            // 没查到，说明账号或密码错了
            return Result.error("账号或密码错误");
        }

        // 3. 查到了，登录成功！返回用户的信息（不包含密码）
        user.setPassword(null); // 为了安全，不要把密码返给前端
        return Result.success(user);
    }

    // 4. 注册接口 (POST)
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        // 1. 先检查用户名是否已经存在
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());

        // selectCount 用于查询符合条件的记录数
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return Result.error("用户名已存在，换一个吧！");
        }

        // 2. 如果不存在，设置默认信息并保存
        user.setCreateTime(java.time.LocalDateTime.now());
        // 如果没填昵称，默认和用户名一样
        if (user.getNickname() == null || user.getNickname().isEmpty()) {
            user.setNickname(user.getUsername());
        }

        userMapper.insert(user);

        return Result.success(user);
    }
}