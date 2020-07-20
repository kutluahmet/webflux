package com.textkernel.javatask.config;


import com.textkernel.javatask.constants.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * @author AKUTLU
 * Configuration class to create a webclient for connecting Integration Service
 */
@Configuration
public class TKClientConfig {


    @Value("${textkernel.service.baseurl}")
    private String baseUrl;

    @Value("${textkernel.service.account}")
    private  String account;

    @Value("${textkernel.service.username}")
    private  String username;

    @Value("${textkernel.service.password}")
    private  String password;

    @Bean
    public WebClient webClient() {

        return WebClient.builder().baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                .build();
    }

    @Bean
    public MultiValueMap<String, Object> apiParameter(){

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.set(Constants.ACCOUNT, account);
        map.set(Constants.USERNAME, username);
        map.set(Constants.PASSWORD, password);
        return map;
    }
}
