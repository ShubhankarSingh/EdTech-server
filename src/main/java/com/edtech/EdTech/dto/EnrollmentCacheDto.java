package com.edtech.EdTech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentCacheDto implements Serializable {

    private static final long serialVersionUID = 3L;

    private Long enrollmentId;
    private LocalDate enrollmentDate;
    private CourseCacheDto course;
}
