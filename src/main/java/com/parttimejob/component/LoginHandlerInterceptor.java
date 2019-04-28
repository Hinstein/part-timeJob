package com.parttimejob.component;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.component
 * @Author: Hinstein
 * @CreateTime: 2019-03-05 12:38
 * @Description:登录检查
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {

    /**
     * 拦截器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("username");
        if (user == null) {
            //未登录，返回登录页面
            request.getRequestDispatcher("/workerLogin").forward(request, response);
            return false;
        } else {
            //已登录，放行请求
            return true;
        }
    }
}
