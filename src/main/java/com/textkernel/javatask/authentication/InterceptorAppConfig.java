package com.textkernel.javatask.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author AKUTLU
 * An Interceptor class for url based token authentication service
 */
@Component
@RequiredArgsConstructor
public class InterceptorAppConfig implements WebMvcConfigurer {

    private final TokenServiceInterceptor tokenServiceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenServiceInterceptor);
    }
}