package com.edtech.EdTech.controller;


import com.edtech.EdTech.dto.CourseDto;
import com.edtech.EdTech.exception.ItemNotFoundException;
import com.edtech.EdTech.model.courses.Category;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.users.User;
import com.edtech.EdTech.repository.UserRepository;
import com.edtech.EdTech.service.CourseService;
import com.edtech.EdTech.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.tomcat.util.codec.binary.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173/")
@RequestMapping("/courses")
public class CourseController {


    private final CourseService courseService;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/add-course")
    public ResponseEntity<?> addCourse(@Valid
            @RequestParam("userId") Long userId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("shortDescription") String shortDescription,
            @RequestParam("language") String language,
            @RequestParam("createdDate") LocalDate createdDate,
            @RequestParam("id") Long categoryId,
            @RequestParam("thumbnail") MultipartFile thumbnail) throws IOException, SQLException {

        User theUser = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        CourseDto courseDto = new CourseDto();
        courseDto.setAuthor(theUser);
        courseDto.setTitle(title);
        courseDto.setDescription(description);
        courseDto.setShortDescription(shortDescription);
        courseDto.setLanguage(language);
        courseDto.setCreatedDate(createdDate);
        courseDto.setId(categoryId);

        Course savedCourse = courseService.addNewCourse(courseDto, thumbnail);

        return ResponseEntity.ok(savedCourse);
    }

    //Get all courses by category
    @GetMapping("/{category}")
    public ResponseEntity<?> getAllCoursesByCategory(@PathVariable String category){
        try {
            List<Course> courses = courseService.getAllCoursesByCategory(category);

            List<CourseDto> result = new ArrayList<>();
            for(Course course: courses ){

                byte[] photoBytes = courseService.getThumbnailByCourseId(course.getId());
                CourseDto courseDto = new CourseDto();
                courseDto.setId(course.getCategory().getId());
                courseDto.setTitle(course.getTitle());
                courseDto.setAuthor(course.getAuthor());
                courseDto.setShortDescription(course.getShortDescription());
                courseDto.setDescription(course.getDescription());
                courseDto.setLanguage(course.getLanguage());
                courseDto.setCreatedDate(course.getCreatedDate());
                courseDto.setVideos(course.getVideos());

                if (photoBytes != null && photoBytes.length > 0) {
                    String base64Photo = Base64.encodeBase64String(photoBytes);
                    courseDto.setThumbnail(base64Photo);
                }

                result.add(courseDto);

            }
            return ResponseEntity.ok(result);
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

    @PutMapping("/update/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable Long courseId, @RequestBody CourseDto courseDto){
        try{
            Course theCourse = courseService.updateCourse(courseId, courseDto);
            return ResponseEntity.ok(theCourse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long courseId){
        try{
            courseService.deleteCourse(courseId);
            return ResponseEntity.ok("Course with id " + courseId + " deleted successfully.");
        }catch (ItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
