package com.edtech.EdTech.service;

import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.courses.Enrollment;
import com.edtech.EdTech.model.users.User;

import java.util.List;

public interface EnrollmentService {

    void enrollCourse(Long userId, Long courseId);

    List<Enrollment> getEnrolledCourses(Long userId);

    boolean checkEnrollmentStatus(Long userId, Long courseId);
}
