package hello;

import entity.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import service.SysMenuService;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 校验url需要什么权限的业务类
 */
@Service
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，
     * 则返回给 decide 方法，用来判定用户是否有此权限。（AccessDecisionManager）
     * 如果不在权限表中则放行。（return null）
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取路径
        String requestUrl=((FilterInvocation)object).getRequestUrl();
        //排除的路径
        if("/login".equals(requestUrl)){
            return null;
        }
        //获取该路径需要的权限
        List<SysMenu> list=sysMenuService.getUrlRole(requestUrl);
        //路径比较器
//        AntPathMatcher matcher=new AntPathMatcher();
        //添加可以通过验证的等级数组
        String[] values=new String[list.size()];
        //循环添加等级名到数组
        for (int i=0;i<list.size();i++){
            values[i]=list.get(i).getRoleName();
        }
        //通过SecurityConfig生成权限列表Collection<ConfigAttribute>
        return SecurityConfig.createList(values);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * 此处要返回true，否则MyFilterSecurityInterceptor类的SecurityMetadataSource
     * 启动会报错：SecurityMetadataSource does not support secure object class
     * @param clazz
     * @return
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
