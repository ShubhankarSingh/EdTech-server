package com.edtech.EdTech.dto;

import com.edtech.EdTech.model.courses.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private String title;
    private String author;
    private String shortDescription;
    private String description;
    private String language;
    private LocalDate createdDate;
    private Long categoryId;

}
