package com.edtech.EdTech.service;

import com.edtech.EdTech.model.courses.Course;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService implements CourseServiceImpl{
    @Override
    public Course addNewCourse() {
        return null;
    }

    @Override
    public List<Course> getAllCourses() {
        return null;
    }

    @Override
    public List<Course> getAllCoursesByCategory() {
        return null;
    }

    @Override
    public Optional<Course> getCourseById() {
        return Optional.empty();
    }

    @Override
    public void DeleteCourse(Long courseId) {

    }

    @Override
    public Course updateCourse() {
        return null;
    }
}
