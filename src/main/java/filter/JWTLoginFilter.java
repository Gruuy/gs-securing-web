package filter;

import entity.SysUser;
import entity.SystemUser;
import hello.AnyUserService;
import hello.ConstantUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 过滤器，这里是一开始的入口，仅仅把这个用户的信息封装成token。。
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    Logger log= LoggerFactory.getLogger(JWTLoginFilter.class);

    private AuthenticationManager authenticationManager;
    private AnyUserService anyUserService;

    public JWTLoginFilter(AuthenticationManager authenticationManager, AnyUserService anyUserService) {
        this.authenticationManager = authenticationManager;
        this.anyUserService = anyUserService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        /** 判断入口 */
        if(!request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("Must use Post!");
        }
        /** 获取用户信息 */
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        log.info(username);
        log.info(password);
        UserDetails user=anyUserService.loadUserByUsername(username);
        if(user!=null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, null);
            return authenticationManager.authenticate(token);
        }else{
            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            try {
                PrintWriter writer=response.getWriter();
                writer.println("账号不存在！");
                writer.flush();
                writer.close();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("success");
        //设置请求头
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        String token="";
        try{
            //getPrincipal是获取请求来的用户信息，可以把它转成一个UserDetail对象
            String username= ((SystemUser)authResult.getPrincipal()).getUsername();
            Date date=new Date(System.currentTimeMillis()+ ConstantUtil.EXPIRE_TIME);
            token= Jwts.builder()
                    .setSubject(username)
                    .setExpiration(date)
                    .signWith(SignatureAlgorithm.HS512,ConstantUtil.TOKEN_KEY)
                    .compact();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Map<String,String> map=new HashMap<>();
        map.put("token",token);
        map.put("message","登陆成功！");
        PrintWriter writer=response.getWriter();
        writer.println(map);
        writer.flush();
        writer.close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("fail");
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        PrintWriter writer=response.getWriter();
        writer.println("账号或密码错误！");
        writer.flush();
        writer.close();
    }
}
