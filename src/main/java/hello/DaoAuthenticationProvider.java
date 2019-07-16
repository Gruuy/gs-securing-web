//package hello;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.stereotype.Component;
//
//
///** filter之后就到了这里 这里对filter封装的token进行信息提取之后验证 */
//@Component
//public class DaoAuthenticationProvider implements AuthenticationProvider {
//
//    Logger log= LoggerFactory.getLogger(DaoAuthenticationProvider.class);
//
//    @Autowired
//    private AnyUserService anyUserService;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username=authentication.getName();
//        String password=authentication.getCredentials().toString();
//        //anyUserService.loadUserByUsername(username);
//        log.info(username);
//        log.info(password);
//        return null;
//    }
//
//    /** 其中的supports()方法接受一个authentication参数，用来判断传进来的authentication是不是该AuthenticationProvider能够处理的类型。 */
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}
