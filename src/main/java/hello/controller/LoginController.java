package hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @RequestMapping("/boom")
    @ResponseBody
    public Map<Object,Object> error(){
        Map<Object,Object> map=new HashMap<>();
        map.put("error_msg","账号或密码错误！");
        return map;
    }

    @RequestMapping("/sayhello")
    @ResponseBody
    public Map<Object,Object> sayHello(){
        Map<Object,Object> map=new HashMap<>();
        map.put("message","wdnmd！");
        return map;
    }
}
