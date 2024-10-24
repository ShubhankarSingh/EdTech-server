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
            List<Enrollment> enrollments = enrollmentService.getEnrolledCourses(userId);

            List<EnrollmentDto> result = new ArrayList<>();
            for(Enrollment enrollment: enrollments){

                EnrollmentDto enrollmentDto = new EnrollmentDto();
                enrollmentDto.setEnrollmentId(enrollment.getEnrollmentId());
                enrollmentDto.setEnrollmentDate(enrollment.getEnrollmentDate());

                byte[] photoBytes = courseService.getThumbnailByCourseId(enrollment.getCourse().getId());
                CourseDto courseDto = new CourseDto();
                courseDto.setCourseId(enrollment.getCourse().getId());
                courseDto.setCategoryId(enrollment.getCourse().getCategory().getId());
                courseDto.setTitle(enrollment.getCourse().getTitle());

                UserDisplayDto userDisplayDto = userServiceImpl.mapToUserDto(enrollment.getCourse().getAuthor());
                courseDto.setAuthor(userDisplayDto);

                if (photoBytes != null && photoBytes.length > 0) {
                    String base64Photo = Base64.encodeBase64String(photoBytes);
                    courseDto.setThumbnail(base64Photo);
                }

                enrollmentDto.setCourse(courseDto);
                result.add(enrollmentDto);
            }

            return ResponseEntity.ok(result);
        }catch (ItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching enrolled courses: " + e.getMessage());
        }
    }
}
