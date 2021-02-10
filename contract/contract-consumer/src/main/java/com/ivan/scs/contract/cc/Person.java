package com.ivan.scs.contract.cc;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Person {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final LocalDate dob;

}
