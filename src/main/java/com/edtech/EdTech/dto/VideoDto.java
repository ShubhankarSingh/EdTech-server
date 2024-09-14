package com.edtech.EdTech.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {

    @NotEmpty(message = "Video title is required")
    private String title;

    @NotEmpty(message = "Video title is required")
    private String url;

}
