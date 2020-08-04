package com.vuedemo.controller;

import java.time.LocalDateTime;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vuedemo.common.http.Result;
import com.vuedemo.common.shiro.AccountProfile;
import com.vuedemo.entity.Blog;
import com.vuedemo.service.BlogService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qipd
 * @since 2020-07-31
 */
@Api(tags = "博客管理")
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;


    @ApiOperation("博客列表")
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "5") Integer pageSize,
        @RequestParam(defaultValue = "1") Integer pageNum) {

        Page<Blog> blogPage = new Page<>(pageNum, pageSize);

        Page<Blog> list = blogService.page(blogPage, new QueryWrapper<Blog>().orderByAsc("Created"));

        return Result.success(list);
    }

    @ApiOperation("博客详情")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable("id") Long id) {

        Blog blog = blogService.getById(id);
        if (blog == null) {
            return Result.fail("文章不存在！");
        }
        return Result.success(blog);
    }

    @ApiOperation("新增修改博客")
    @RequiresAuthentication
    @GetMapping("/edit")
    public Result edit(@Validated @RequestBody Blog blog) {

        Long currrentUserId = ((AccountProfile)SecurityUtils.getSubject().getPrincipal()).getId();
        Blog temp = null;
        if (StrUtil.isEmpty(blog.getId().toString())) {
            // 新增
            temp = new Blog();
            temp.setUserId(currrentUserId);
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        } else {
            // 修改
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            if (!temp.getUserId().equals(currrentUserId.longValue())) {
                return Result.fail("只能改自己的文章");
            }
        }

        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
        blogService.saveOrUpdate(temp);

        return Result.success(blog);
    }

}
