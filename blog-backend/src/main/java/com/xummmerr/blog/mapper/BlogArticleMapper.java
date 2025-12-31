package com.xummmerr.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xummmerr.blog.entity.BlogArticle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogArticleMapper extends BaseMapper<BlogArticle> {
}