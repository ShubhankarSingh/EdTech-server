package com.edtech.EdTech.repository;

import com.edtech.EdTech.model.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {


    List<Course> findCoursesByCategoryId(Long categoryId);

    Optional<Course> findCourseByTitle(String title);
}
