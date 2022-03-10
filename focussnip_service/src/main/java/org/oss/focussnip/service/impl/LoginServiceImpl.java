package org.oss.focussnip.service.impl;

import org.oss.focussnip.mapper.LoginMapper;
import org.oss.focussnip.model.UserLogin;
import org.oss.focussnip.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public UserLogin test() {
        return loginMapper.selectList(null).get(0);
    }
}
