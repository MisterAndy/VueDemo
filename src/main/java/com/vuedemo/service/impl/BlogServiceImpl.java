package com.vuedemo.service.impl;

import com.vuedemo.entity.Blog;
import com.vuedemo.mapper.BlogMapper;
import com.vuedemo.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qipd
 * @since 2020-07-31
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
