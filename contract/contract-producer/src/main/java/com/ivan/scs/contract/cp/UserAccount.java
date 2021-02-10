package com.ivan.scs.contract.cp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserAccount {

    private Long id;
    private final String firstName;
    private final String lastName;
    private final LocalDate dob;

}
