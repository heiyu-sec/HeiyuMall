package com.heiyu.mall.config;

import com.heiyu.mall.filter.AdminFilter;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

/**
 * 描述： admin过滤器
 */

@Configuration
public class AdminFilterConfig {
    @Bean
    public AdminFilter adminFilter(){
        return new AdminFilter();
    }
    @Bean(name = "adminFilterConf")
    public FilterRegistrationBean adminFilterConfig(){
        FilterRegistrationBean filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(adminFilter());
        filterFilterRegistrationBean.addUrlPatterns("/admin/category/*");
        filterFilterRegistrationBean.addUrlPatterns("/admin/product/*");
        filterFilterRegistrationBean.addUrlPatterns("/admin/order/*");
        filterFilterRegistrationBean.setName("adminFilterConfig");
        return filterFilterRegistrationBean;
    }
}
