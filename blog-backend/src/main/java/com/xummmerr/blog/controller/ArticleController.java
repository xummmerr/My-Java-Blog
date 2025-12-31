package com.xummmerr.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xummmerr.blog.entity.BlogArticle;
import com.xummmerr.blog.mapper.BlogArticleMapper;
import com.xummmerr.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private BlogArticleMapper articleMapper;

    // 1. 发布文章接口 (POST 请求)
    @PostMapping("/add")
    public Result add(@RequestBody BlogArticle article) {
        // 暂时先把作者写死是 admin (ID=1)，后面做登录功能再改
        article.setUserId(1L);
        articleMapper.insert(article);
        return Result.success("发布成功");
    }

    // 2. 获取所有文章列表 (GET 请求)
    @GetMapping("/list")
    public List<BlogArticle> list() {
        return articleMapper.selectList(null);
    }

    // 3. 删除文章接口 (DELETE 请求)
    // 路径里的 {id} 是占位符，对应方法参数里的 @PathVariable Long id
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        articleMapper.deleteById(id);
        return Result.success("删除成功");
    }

    // 4. 根据ID查询文章详情 (GET)
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id) {
        BlogArticle article = articleMapper.selectById(id);
        return Result.success(article);
    }

    // 5、 修改文章接口
    // ==========================================
    @PostMapping("/update")
    public Result update(@RequestBody BlogArticle article) {
        // updateById 是 MyBatis-Plus 的神器
        // 它会自动根据传入的 id 找到那行数据，并把其他字段（title, content）更新进去
        articleMapper.updateById(article);
        return Result.success("修改成功");
    }

    // 6. 分页搜索文章 (GET)
    // 改动点：增加了 pageNum 和 pageSize 参数
    @GetMapping("/search")
    public Result search(
            @RequestParam(defaultValue = "1") int pageNum,   // 第几页，默认1
            @RequestParam(defaultValue = "10") int pageSize, // 每页几条，默认10
            @RequestParam(required = false) String keyword) { // 搜索词

        // 1. 创建分页对象 (这是 MyBatis-Plus 的核心)
        Page<BlogArticle> pageInfo = new Page<>(pageNum, pageSize);

        // 2. 构建查询条件 (和原来一样)
        LambdaQueryWrapper<BlogArticle> queryWrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.like(BlogArticle::getTitle, keyword)
                    .or()
                    .like(BlogArticle::getContent, keyword);
        }
        queryWrapper.orderByDesc(BlogArticle::getCreateTime); // 保持倒序

        // 3. 执行分页查询
        // 注意：这里变成了 selectPage，而不是 selectList
        // 它会自动在 SQL 后面加上 LIMIT 语句
        articleMapper.selectPage(pageInfo, queryWrapper);

        // 4. 返回结果
        // 前端拿到的是 pageInfo，里面包含了 records(数据列表) 和 total(总条数)
        return Result.success(pageInfo);
    }
}