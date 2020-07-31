package com.vuedemo.service.impl;

import com.vuedemo.entity.User;
import com.vuedemo.mapper.UserMapper;
import com.vuedemo.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
