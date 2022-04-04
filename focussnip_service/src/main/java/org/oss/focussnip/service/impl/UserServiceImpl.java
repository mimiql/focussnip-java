package org.oss.focussnip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.oss.focussnip.mapper.UserMapper;
import org.oss.focussnip.model.Users;
import org.oss.focussnip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, Users> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Users test() {
        return userMapper.selectList(null).get(0);
    }

    @Override
    public Set<String> getRoles(String username) {
        List<String> roles = userMapper.getRoles(username);
        return new HashSet<>(roles);
    }

    @Override
    public Set<String> getPermissions(String username) {
        List<String> permissions = userMapper.getPermissions(username);
        return new HashSet<>(permissions);
    }

    @Override
    public Users getByUsername(String username) {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return userMapper.selectOne(wrapper);
    }
}
