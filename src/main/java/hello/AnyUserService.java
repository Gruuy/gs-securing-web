package hello;

import entity.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/** 一定要实现这个接口，不然会gg的 */
@Service
public class AnyUserService implements UserDetailsService {

    @Autowired
    private AnyUserMapper anyUserMapper;

    /** 重写的这个方法仅仅用于判断这个用户存不存在 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser user=anyUserMapper.getUserByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("用户不存在！");
        }
        return user;
    }
}
