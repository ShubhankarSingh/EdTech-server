package com.edtech.EdTech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto implements Serializable {

    private static final long serialVersionUID = 3L;

    private Long enrollmentId;
    private LocalDate enrollmentDate;
    private CourseDto course;
}
