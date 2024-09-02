package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.CourseDto;
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
    public Course addNewCourse(CourseDto courseDto) {
        try{
            Course theCourse = new Course();
            theCourse.setAuthor(courseDto.getAuthor());
            theCourse.setTitle(courseDto.getTitle());
            theCourse.setShortDescription(courseDto.getShortDescription());
            theCourse.setDescription(courseDto.getDescription());
            theCourse.setLanguage(courseDto.getLanguage());
            theCourse.setCreatedDate(courseDto.getCreatedDate());

            Category category = categoryRepository.findById(courseDto.getCategoryId())
                    .orElseThrow(()-> new RuntimeException("Category not found with ID: " + courseDto.getCategoryId()));
            theCourse.setCategory(category);

            return courseRepository.save(theCourse);
        }catch (Exception e){
            throw new RuntimeException("An error occurred while saving the course: " + e.getMessage());
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
    public void deleteCourse(Long courseId) {
        Course theCourse = courseRepository.findById(courseId)
                .orElseThrow(()-> new ItemNotFoundException("No course found with id: " + courseId));

        courseRepository.deleteById(courseId);

    }

    @Override
    public Course updateCourse(Long courseId, CourseDto courseDto) {
        try{
            Course theCourse = courseRepository.findById(courseId)
                    .orElseThrow(()-> new ItemNotFoundException("No course found with id: " + courseId));

            if(courseDto.getAuthor() != null) theCourse.setAuthor(courseDto.getAuthor());
            if(courseDto.getTitle() != null) theCourse.setTitle(courseDto.getTitle());
            if(courseDto.getDescription() != null) theCourse.setDescription(courseDto.getDescription());
            if(courseDto.getShortDescription() != null) theCourse.setShortDescription(courseDto.getShortDescription());
            if(courseDto.getLanguage() != null) theCourse.setLanguage(courseDto.getLanguage());
            if(courseDto.getCreatedDate() != null) theCourse.setCreatedDate(courseDto.getCreatedDate());

            Category category = categoryRepository.findById(courseDto.getCategoryId())
                    .orElseThrow(()-> new RuntimeException("Category not found with ID: " + courseDto.getCategoryId()));
            theCourse.setCategory(category);

            return courseRepository.save(theCourse);
        }catch (Exception e){
            throw new RuntimeException("An error occurred while saving the course: " + e.getMessage());
        }
    }
}
