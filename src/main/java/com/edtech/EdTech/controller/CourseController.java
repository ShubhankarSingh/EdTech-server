package com.edtech.EdTech.controller;


import com.edtech.EdTech.exception.ItemNotFoundException;
import com.edtech.EdTech.model.courses.Category;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {


    private final CourseService courseService;

    @PostMapping("/add-course")
    public ResponseEntity<?> addCourse(@RequestBody Course course){
        try{
            System.out.println("Course body: " + course);
            Course theCourse = courseService.addNewCourse(course);
            return ResponseEntity.ok(theCourse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Get all courses by category
    @GetMapping("/{category}")
    public ResponseEntity<?> getAllCoursesByCategory(@PathVariable String category){
        try {
            List<Course> courses = courseService.getAllCoursesByCategory(category);
            return ResponseEntity.ok(courses);
        }catch (ItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Get a course by title
    @GetMapping("/course/{title}")
    public ResponseEntity<?> getCourseByTitle(@PathVariable String title){
        try{
            Optional<Course> theCourse = courseService.getCourseByTitle(title);
            return ResponseEntity.ok(theCourse);
        }catch (ItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
