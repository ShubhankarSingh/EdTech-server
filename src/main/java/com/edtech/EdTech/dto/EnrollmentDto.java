package com.edtech.EdTech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {

    private Long enrollmentId;
    private LocalDate enrollmentDate;

    private CourseDto course;

}
