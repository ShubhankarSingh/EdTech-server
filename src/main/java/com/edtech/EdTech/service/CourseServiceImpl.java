package com.edtech.EdTech.service;

import com.edtech.EdTech.exception.ItemNotFoundException;
import com.edtech.EdTech.model.courses.Category;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.repository.CategoryRepository;
import com.edtech.EdTech.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Course addNewCourse() {
        return null;
    }

    @Override
    public List<Course> getAllCourses() {
        return null;
    }

    @Override
    public List<Course> getAllCoursesByCategory(String category) {
        Category theCategory = categoryRepository.findByCategoryType(category);
        if(theCategory == null){
            throw new ItemNotFoundException("We can’t find the page you’re looking for.");
        }
        List<Course> courses = courseRepository.findCoursesByCategoryId(theCategory.getId());

        return courses;
    }

    @Override
    public Optional<Course> getCourseById() {
        return Optional.empty();
    }

    @Override
    public Optional<Course> getCourseByTitle(String title) {
        Optional<Course> theCourse = courseRepository.findCourseByTitle(title);
        if(theCourse.isEmpty()){
            throw new ItemNotFoundException("We can’t find the page you’re looking for.");
        }
        return theCourse;
    }

    @Override
    public void DeleteCourse(Long courseId) {

    }

    @Override
    public Course updateCourse() {
        return null;
    }
}
