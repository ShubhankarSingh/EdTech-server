package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.EnrollmentDto;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.courses.Enrollment;
import com.edtech.EdTech.model.users.User;

import java.sql.SQLException;
import java.util.List;

public interface EnrollmentService {

    void enrollCourse(Long userId, Long courseId);

    List<EnrollmentDto> getEnrolledCourses(Long userId) throws SQLException;

    boolean checkEnrollmentStatus(Long userId, Long courseId);
}
