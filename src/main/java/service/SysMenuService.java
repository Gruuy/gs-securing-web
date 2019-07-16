package service;

import entity.SysMenu;

import java.util.List;

public interface SysMenuService {
    /**
     * 路径权限获取
     */
    List<SysMenu> getUrlRole(String url);
}
