package com.heiyu.mall.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.heiyu.mall.common.ApiRestResponse;
import com.heiyu.mall.common.Constant;
import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.exctption.ImoocMallExceptionEnum;
import com.heiyu.mall.model.pojo.Category;
import com.heiyu.mall.model.pojo.User;
import com.heiyu.mall.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.jws.soap.SOAPBinding;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

import static com.heiyu.mall.filter.UserFilter.currentUser;

/**
 * 描述： 管理员校验过滤器
 */
public class AdminFilter implements Filter {
    @Autowired
    UserService userService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if ("OPTIONS".equals(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String token = request.getHeader(Constant.JWT_TOKEN);
            if (StringUtils.isEmpty(token)) {
                PrintWriter out = new HttpServletResponseWrapper(
                        (HttpServletResponse) servletResponse).getWriter();
                out.write("{\n"
                        + "    \"status\": 10007,\n"
                        + "    \"msg\": \"NEED_JWT_TOKEN\",\n"
                        + "    \"data\": null\n"
                        + "}");
                out.flush();
                out.close();
                return;
            }

            Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            try {
                DecodedJWT jwt = verifier.verify(token);

                currentUser.setId(jwt.getClaim(Constant.USER_ID).asInt());
                currentUser.setRole(jwt.getClaim(Constant.USER_ROLE).asInt());
                currentUser.setUsername(jwt.getClaim(Constant.USER_NAME).asString());

            } catch (TokenExpiredException e) {
                //token过期，抛出异常
                throw new ImoocMallException(ImoocMallExceptionEnum.TOKEN_EXPIRED);
            } catch (JWTDecodeException e) {
                //解码失败，抛出异常
                throw new ImoocMallException(ImoocMallExceptionEnum.TOKEN_WRONG);
            }

            //校验是否是管理员
            boolean adminRole = userService.checkAdminRole(UserFilter.currentUser);
            if (adminRole) {
                filterChain.doFilter(servletRequest, resp);
            } else {
                PrintWriter out = new HttpServletResponseWrapper(
                        (HttpServletResponse) servletResponse).getWriter();
                out.write("{\n"
                        + "    \"status\": 10009,\n"
                        + "    \"msg\": \"NEED_ADMIN\",\n"
                        + "    \"data\": null\n"
                        + "}");
                out.flush();
                out.close();
            }
        }

    }

    @Override
    public void destroy() {

    }
}

