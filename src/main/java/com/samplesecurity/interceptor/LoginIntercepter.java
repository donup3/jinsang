package com.samplesecurity.interceptor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class LoginIntercepter implements HandlerInterceptor {
    //delete/1/2 이렇게 두개 들어가면 **두개 해줘야함.
    public List<String> loginEssential = Arrays.asList(
            "/jinsang/addAgree",
            "/jinsang/reply/write/*",
            "/jinsang/reply/write/subReply/**",
            "/jinsang/reply/delete/**",
            "/jinsang/reply/addAgree/**",
            "/jinsang/reply/disAgree/**"
    ); //로그인 필요한 url
    public List loginInessential = Arrays.asList(); //로그인 필요없는 url

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("---Interceptor---");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal().equals("anonymousUser")) {//로그인 유저가 아닐 떄
            if (isAjaxRequest(request)) {
                System.out.println("true ajax");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED); //401code를  줌
                return false;
            } else {
                response.sendRedirect("/user/login");
                return false;
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private boolean isAjaxRequest(HttpServletRequest req) {
        String header = req.getHeader("AJAX");
        return "true".equals(header);
    }
}
