package chengweiou.universe.andromeda.interceptor;

import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import com.google.gson.Gson;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthInterceptorMg implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (checkInServer(request)) return true;
        return unauth(response);
    }

    private boolean unauth(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(new Gson().toJson(Rest.fail(BasicRestCode.UNAUTH)));
        return false;
    }

    private boolean checkInServer(HttpServletRequest request) {
        return Boolean.valueOf(request.getHeader("inServer"));
    }
}
