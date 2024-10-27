package com.edtech.EdTech.controller;

import com.edtech.EdTech.dto.CourseDto;
import com.edtech.EdTech.dto.EnrollmentDto;
import com.edtech.EdTech.dto.UserDisplayDto;
import com.edtech.EdTech.exception.ItemNotFoundException;
import com.edtech.EdTech.model.courses.Enrollment;
import com.edtech.EdTech.service.CourseService;
import com.edtech.EdTech.service.EnrollmentService;
import com.edtech.EdTech.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class EnrollmentController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final UserServiceImpl userServiceImpl;

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

    @GetMapping("/isEnrolled/{courseId}")
    public ResponseEntity<?> checkEnrollmentStatus(@RequestParam("userId") Long userId,
                                                   @PathVariable("courseId") Long courseId){
        try{
            boolean isEnrolled = enrollmentService.checkEnrollmentStatus(userId, courseId);
            return ResponseEntity.ok(isEnrolled);
        }catch (ItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred fetching enrollment status: " + e.getMessage());
        }
    }

    @GetMapping("/enrolled-courses")
    public ResponseEntity<?> getEnrolledCourses(@RequestParam("userId") Long userId){

        try{
            List<EnrollmentDto> enrollments = enrollmentService.getEnrolledCourses(userId);

            return ResponseEntity.ok(enrollments);
        }catch (ItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching enrolled courses: " + e.getMessage());
        }
    }
}
