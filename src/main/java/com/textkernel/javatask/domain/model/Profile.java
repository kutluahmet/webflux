package com.textkernel.javatask.domain.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author AKUTLU
 * Model class for Profile
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Profile {

    private String FirstName;
    private String LastName;
    private Address Address;

}
