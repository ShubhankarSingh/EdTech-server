package com.edtech.EdTech.service;

import com.edtech.EdTech.model.courses.Category;
import com.edtech.EdTech.model.courses.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Course addNewCourse();

    List<Course> getAllCourses();
    List<Course> getAllCoursesByCategory(String category);

    Optional<Course> getCourseById();

    void DeleteCourse(Long courseId);

    Course updateCourse();
}
