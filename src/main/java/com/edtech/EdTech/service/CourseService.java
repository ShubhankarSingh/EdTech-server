package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.CourseDto;
import com.edtech.EdTech.model.courses.Category;
import com.edtech.EdTech.model.courses.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Course addNewCourse(CourseDto courseDto);

    List<Course> getAllCourses();
    List<Course> getAllCoursesByCategory(String category);

    Optional<Course> getCourseById();

    Optional<Course> getCourseByTitle(String title);

    void DeleteCourse(Long courseId);

    Course updateCourse();
}
