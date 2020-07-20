package com.textkernel.javatask.config;


import com.textkernel.javatask.domain.document.User;
import com.textkernel.javatask.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class InitDatabaseConfig {

    private final UserRepository userRepository;

    public InitDatabaseConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${textkernel.proxy.username}")
    private  String username;

    @Value("${textkernel.proxy.password}")
    private  String password;


    @Bean
    public User createUser() {

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user).subscribe();
        log.info("User: {} has been added to Database ", username);
        return user;
    }

}
