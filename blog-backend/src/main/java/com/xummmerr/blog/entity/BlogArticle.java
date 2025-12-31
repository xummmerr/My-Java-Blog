package com.xummmerr.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("blog_article")
public class BlogArticle {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String summary;
    private String content;
    private Long userId; // 作者ID
    private Long categoryId; // 分类ID

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}