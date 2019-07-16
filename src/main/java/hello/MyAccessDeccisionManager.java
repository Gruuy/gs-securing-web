package hello;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * 通过校验用户的权限，以及判断是否属于AnonymousAuthenticationToken类型，进行权限认证，如果都符合，则return，不抛出异常
 */
@Component
public class MyAccessDeccisionManager implements AccessDecisionManager {
    /**
     * decide 方法是判定是否拥有权限的决策方法
     * @param authentication 是用户的token  如果是UsernamePasswordAuthenticationToken  则是登陆了的用户
     *                       注意在UserDetail里面要重写赋值角色的方法
     *                       如果没有登录，Security默认会给一个AnonymousAuthenticationToken类型
     * @param configAttributes  是路径要求的角色名称，以ROLE_开头的
     *                          已登录的用户token里面可以获取用户角色名，与此比对，就可知道用户是否拥有权限
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        String needRole="";
        //判断是否登录
        if(authentication instanceof AnonymousAuthenticationToken){
            //BadCredentialsException是自定义异常
            throw new BadCredentialsException("未登录！");
        }
        //如果有可访问的角色名
        if(configAttributes!=null&&configAttributes.size()>0){
            //因为是Collection，所以要用遍历器
            Iterator<ConfigAttribute> iterator=configAttributes.iterator();
            //循环
            while (iterator.hasNext()){
                //  对我们传入的String[]做的封装
                ConfigAttribute next=iterator.next();
                //  就是String数组里的每一个元素
                needRole=next.getAttribute();
                //  循环用户拥有的角色列表
                for(GrantedAuthority authority:authentication.getAuthorities()){
                    //  如果相等，放行
                    if(needRole.trim().equals(authority.getAuthority())){
                        return;
                    }
                }
            }
            //循环了没有，证明权限不够啦
            throw new AccessDeniedException("权限不足！");
        }
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
