package service;

import entity.SystemUser;

public interface SysUserService {
    /**
     * 登陆
     * */
    SystemUser login(String username,String password);
}
