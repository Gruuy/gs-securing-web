package hello.dao;

import entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper {

    /**
     * 获取路径所需要的权限等级
     */
    List<SysMenu> getUrlRole(@Param("url") String url);
}
