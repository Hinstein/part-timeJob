package com.parttimejob.config;

import com.parttimejob.component.LoginHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.config
 * @Author: Hinstein
 * @CreateTime: 2019-03-05 13:25
 * @Description:
 */


@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("/index");
//    }
//

    /**
     * 注册拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //静态资源也会被拦截，所以需要加"/assets/**","/webjars/**"
        String[] excludeUrl = new String[]{"/index", "/assets/**", "/webjars/**", "/blog/**"
                , "/workerLogin", "/managerLogin", "/workerRegister", "/managerRegister",
                "/admin/login", "/manager/register", "/manager/login", "/worker/register",
                "/worker/login", "/webjars/**", "/defaultKaptcha"};
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns(excludeUrl);
    }
}
