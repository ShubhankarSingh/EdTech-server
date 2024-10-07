package com.edtech.EdTech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateResponseDto {

    private String message;
    private Long id;
    private String title;

}
