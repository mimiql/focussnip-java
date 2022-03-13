package org.oss.focussnip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.oss.focussnip.model.Users;

import java.util.Set;

public interface UserService extends IService<Users> {
    Users test();
    Set<String> getRoles(String username);
    Set<String> getPermissions(String username);

    Users getByUsername(String username);
}
