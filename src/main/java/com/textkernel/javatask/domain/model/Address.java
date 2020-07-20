package com.textkernel.javatask.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * @author AKUTLU
 * Model class for Address
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String StreetName;
    private String StreetNumberBase;
    private String PostalCode;
    private String City;

}
