package hello;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class ResponseUtil {
    /**
     * 相应到前端的信息
     * @param response
     * @param msg
     */
    public static void repsonseSetInfo(HttpServletResponse response, Map<String,String> msg){
        try {
            JSONObject object = JSONObject.fromObject(msg);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(object.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
