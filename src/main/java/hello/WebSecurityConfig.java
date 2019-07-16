package hello;

import filter.JWTAuuthenticationFilter;
import filter.JWTLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private AnyUserService anyUserService;

    @Autowired
    private AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;

    @Autowired
    /** 权限比较业务 */
    private MyAccessDeccisionManager myAccessDeccisionManager;

    @Autowired
    /** 路径需要的权限获取业务 */
    private MyInvocationSecurityMetadataSourceService myInvocationSecurityMetadataSourceService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>( ) {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(myInvocationSecurityMetadataSourceService);
                        object.setAccessDecisionManager(myAccessDeccisionManager);
                        return object;
                    }
                })
                .and()
            .formLogin()
                .loginPage("/login")
//                .failureForwardUrl("/boom")
//                .successForwardUrl("/hello")
                .permitAll()
                .and()
                .addFilter(new JWTLoginFilter(authenticationManager(),anyUserService))
                .addFilter(new JWTAuuthenticationFilter(authenticationManager(),anyUserService))
            .logout()
                .permitAll()
            .and()
                //设置抛出异常的处理类
            .csrf().disable().exceptionHandling().accessDeniedHandler(authenticationAccessDeniedHandler);
    }

    /**由于withDefaultPasswordEncoder被弃用，项目WebSecurityConfig类中会报错，将userDetailsService方法注释换：*/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //配置加密密码的方式
        auth.userDetailsService(anyUserService).passwordEncoder(new BCryptPasswordEncoder());

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        //虚拟用户
        auth.inMemoryAuthentication()
                .withUser("gruuy")
                .password(encoder.encode("123")).roles("admin");
        //把我们自己实现AuthenticationProvider接口的类加到ProviderManager中
//        auth.authenticationProvider(daoAuthenticationProvider);
    }
}