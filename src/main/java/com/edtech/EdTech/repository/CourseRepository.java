package com.edtech.EdTech.repository;

import com.edtech.EdTech.model.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
