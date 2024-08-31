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
    public Course addNewCourse(Course course) {
        try{
            Course theCourse = new Course();
            theCourse.setAuthor(course.getAuthor());
            theCourse.setCategory(course.getCategory());
            theCourse.setTitle(course.getTitle());
            theCourse.setShortDescription(course.getShortDescription());
            theCourse.setDescription(course.getDescription());
            theCourse.setLanguage(course.getLanguage());
            theCourse.setCreatedDate(course.getCreatedDate());

            return courseRepository.save(theCourse);
        }catch (Exception e){
            throw new RuntimeException("An error occurred while saving the user: " + e.getMessage());
        }
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
