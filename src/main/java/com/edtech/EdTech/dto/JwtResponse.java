package com.edtech.EdTech.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {

    private Long id;
    private String email;
    private String name;
    private String token;

    public JwtResponse(Long id, String email, String name, String token) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.token = token;
    }

}
