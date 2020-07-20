package com.textkernel.javatask.domain.document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Model class for User
 * @author AKUTLU
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    @Id
    private String id;

    private String username;

    private String password;

    private String accessToken;

    private boolean tokenExpired;
}
