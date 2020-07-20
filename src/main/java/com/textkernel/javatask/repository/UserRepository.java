package com.textkernel.javatask.repository;

import com.textkernel.javatask.domain.document.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    @Query("{'username' : ?0, 'password' : ?1}")
    Mono<User> findUserByNameAndPassword(String username, String password);
}
