package filter;

import hello.AnyUserService;
import hello.ConstantUtil;
import hello.ResponseUtil;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义JWT认证过滤器
 * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 * @author huangsz  2018/10/24 0024
 */
public class JWTAuuthenticationFilter extends BasicAuthenticationFilter {

    private Logger log= LoggerFactory.getLogger(JWTAuuthenticationFilter.class);

    private AnyUserService anyUserService;

    public JWTAuuthenticationFilter(AuthenticationManager authenticationManager, AnyUserService anyUserService) {
        super(authenticationManager);
        this.anyUserService = anyUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //get token
        String token=request.getHeader("token");
        //如果token为空
        if(StringUtils.isEmpty(token)){
            //接着走，Security自己生成AnonymousAuthenticationToken
            chain.doFilter(request,response);
            return;
        }
        //否则就我们自己生成UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authentication=getAuthenication(request,response,token);
        //生成不出来，就抛异常了，也不往下走，gg了
        if(authentication==null){
            return;
        }
        //生成出来了，就要设置到Security全局容器中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //而且也要继续走下去
        chain.doFilter(request,response);
    }

    /** 把Token里的信息提取出来的方法 */
    private UsernamePasswordAuthenticationToken getAuthenication(HttpServletRequest request,HttpServletResponse response,String token){
        //获取当前时间
        long start=System.currentTimeMillis();
        String user=null;
        //解析Token
        try {
            user = Jwts.parser()
                    .setSigningKey(ConstantUtil.TOKEN_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            long end = System.currentTimeMillis();
            log.info("执行时间为：{}" , (end - start) + "毫秒");
            log.info("用户id为：{}",user);
            log.info("token:{}",token);
            //如果可以解析
            if (user != null && user != "") {
                //获取用户详情并返回
                UserDetails userDetails = anyUserService.loadUserByUsername(user);
                //这个就是decide方法的参数，用户的角色名
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                //返回生成的Token（authentication）
                return new UsernamePasswordAuthenticationToken(user, null,userDetails.getAuthorities());
            } else {
                log.info("Not Login");
                response.setHeader("Content-Type", "text/html;charset=UTF-8");
                PrintWriter writer=response.getWriter();
                writer.println("未登录！");
                writer.flush();
                writer.close();
            }
        }catch (ExpiredJwtException e) {
            Map<String,String> map=new HashMap<>(1);
            map.put("Message","Token已过期");
            ResponseUtil.repsonseSetInfo(response,map);
            return null;
        } catch (UnsupportedJwtException e) {
            Map<String,String> map=new HashMap<>(1);
            map.put("Message","Token格式错误！");
            ResponseUtil.repsonseSetInfo(response,map);
            return null;
        } catch (MalformedJwtException e) {
            Map<String,String> map=new HashMap<>(1);
            map.put("Message","token没有被正确构造！");
            ResponseUtil.repsonseSetInfo(response,map);
            return null;
        } catch (SignatureException e) {
            Map<String,String> map=new HashMap<>(1);
            map.put("Message","签名失败！");
            ResponseUtil.repsonseSetInfo(response,map);
            return null;
        } catch (IllegalArgumentException e) {
            Map<String,String> map=new HashMap<>(1);
            map.put("Message","非法参数异常！");
            ResponseUtil.repsonseSetInfo(response,map);
            return null;
        } catch (IOException e) {
            e.printStackTrace( );
        }
        return null;
    }
}
