package com.heiyu.mall.filter;


import com.heiyu.mall.common.Constant;
import com.heiyu.mall.model.pojo.User;
import com.heiyu.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 描述： 用户校验过滤器
 */
public class UserFilter implements Filter {
    public static User currentUser;

    @Autowired
    UserService userService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        if (currentUser == null) {
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\"status\" : 10007, \"msg\" : \"NEED_LOGIN\", \"data\" : null}");
            out.flush();
            out.close();
            return;

        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    }

