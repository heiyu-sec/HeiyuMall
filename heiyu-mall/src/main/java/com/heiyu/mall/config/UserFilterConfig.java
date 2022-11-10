package com.heiyu.mall.config;

import com.heiyu.mall.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述： user过滤器
 */

@Configuration
public class UserFilterConfig {
    @Bean
    public AdminFilter userFilter(){
        return new AdminFilter();
    }
    @Bean(name = "userFilterConf")
    public FilterRegistrationBean adminFilterConfig(){
        FilterRegistrationBean filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(userFilter());
        filterFilterRegistrationBean.addUrlPatterns("/cart/*");
        filterFilterRegistrationBean.addUrlPatterns("/order/*");

        filterFilterRegistrationBean.setName("userFilterConfig");
        return filterFilterRegistrationBean;
    }
}
