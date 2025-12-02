package com.nt.dto;

import lombok.Data;

@Data
public class UserResponseDTO {


    private String fname;
    
    private String lname;

    private String email;
    
    private String password;

    private Long phone;

    private String address;
}

