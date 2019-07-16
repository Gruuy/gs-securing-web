package hello;

import entity.SysMenu;
import hello.dao.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.SysMenuService;

import java.util.List;

/**
 * @author Gruuy
 * @date 2019-7-15
 * 获取路径权限信息的业务
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 获取路径需要的权限
     * @param url
     * @return
     */
    @Override
    public List<SysMenu> getUrlRole(String url) {
        return sysMenuMapper.getUrlRole(url);
    }
}
