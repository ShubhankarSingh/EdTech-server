package com.edtech.EdTech.controller;

import com.edtech.EdTech.exception.ItemNotFoundException;
import com.edtech.EdTech.model.courses.Enrollment;
import com.edtech.EdTech.service.EnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<String> enrollCourse(@RequestParam("userId") Long userId,
                                          @PathVariable("courseId") Long courseId){

        try{
            enrollmentService.enrollCourse(userId, courseId);
            return ResponseEntity.ok("Enrollment successful!");
        }catch (ItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during enrollment: " + e.getMessage());
        }
    }

    @GetMapping("/enrolled-courses")
    public ResponseEntity<?> getEnrolledCourses(@RequestParam("userId") Long userId){

        try{
            List<Enrollment> enrollments = enrollmentService.getEnrolledCourses(userId);
            return ResponseEntity.ok(enrollments);
        }catch (ItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching enrolled courses: " + e.getMessage());
        }
    }
}
