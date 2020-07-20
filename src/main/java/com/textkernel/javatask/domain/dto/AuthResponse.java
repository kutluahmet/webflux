package com.textkernel.javatask.domain.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


/**
 * @author AKUTLU
 * Model class for  Authentication responses
 */
@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private Integer code;
    private String message;
    private String token;
}
