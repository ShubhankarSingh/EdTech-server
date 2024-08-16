package com.edtech.EdTech.service;

import com.edtech.EdTech.model.courses.Course;

import java.util.List;
import java.util.Optional;

public interface CourseServiceImpl {

    Course addNewCourse();

    List<Course> getAllCourses();

    List<Course> getAllCoursesByCategory();

    Optional<Course> getCourseById();

    void DeleteCourse(Long courseId);

    Course updateCourse();
}
