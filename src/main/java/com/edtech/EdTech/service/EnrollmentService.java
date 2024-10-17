package com.edtech.EdTech.service;

import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.courses.Enrollment;
import com.edtech.EdTech.model.users.User;

public interface EnrollmentService {

    void enrollCourse(Long userId, Long courseId);

}
