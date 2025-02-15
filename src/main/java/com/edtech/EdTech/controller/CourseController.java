package com.edtech.EdTech.controller;


import com.edtech.EdTech.dto.CourseDto;
import com.edtech.EdTech.dto.UpdateResponseDto;
import com.edtech.EdTech.dto.UserDisplayDto;
import com.edtech.EdTech.exception.ItemNotFoundException;
import com.edtech.EdTech.model.courses.Category;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.users.User;
import com.edtech.EdTech.repository.UserRepository;
import com.edtech.EdTech.service.CourseService;
import com.edtech.EdTech.service.UserService;
import com.edtech.EdTech.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
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
    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, CourseDto> recentlyViewedCourseCache;

    @PostMapping("/add-course")
    public ResponseEntity<?> addCourse(@Valid
            @RequestParam("userId") Long userId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("shortDescription") String shortDescription,
            @RequestParam("originalPrice") Long originalPrice,
            @RequestParam("offerPrice") Long offerPrice,
            @RequestParam("language") String language,
            @RequestParam("createdDate") LocalDate createdDate,
            @RequestParam("id") Long categoryId,
            @RequestParam("thumbnail") MultipartFile thumbnail) throws IOException, SQLException {

        User theUser = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        CourseDto courseDto = new CourseDto();
        courseDto.setUserId(theUser.getId());
        courseDto.setTitle(title);
        courseDto.setDescription(description);
        courseDto.setShortDescription(shortDescription);
        courseDto.setOriginalPrice(originalPrice);
        courseDto.setOfferPrice(offerPrice);
        courseDto.setLanguage(language);
        courseDto.setCreatedDate(createdDate);
        courseDto.setCategoryId(categoryId);

        Course savedCourse = courseService.addNewCourse(courseDto, thumbnail);

        return ResponseEntity.ok(savedCourse);
    }

    @QueryMapping
    public List<CourseDto> getAllCoursesByCategory(@Argument String category) {
        try {
            List<Course> courses = courseService.getAllCoursesByCategory(category);

            for (Course course : courses) {
                System.out.println("\n\n\n\nCourse ID: " + course.getId());
            }

            List<CourseDto> result = new ArrayList<>();
            for (Course course : courses) {
                byte[] photoBytes = courseService.getThumbnailByCourseId(course.getId());
                CourseDto courseDto = new CourseDto();
                courseDto.setId(course.getId());
                courseDto.setCategoryId(course.getCategory().getId());
                courseDto.setTitle(course.getTitle());
                courseDto.setOriginalPrice(course.getOriginalPrice());
                courseDto.setOfferPrice(course.getOfferPrice());

                UserDisplayDto userDisplayDto = userServiceImpl.mapToUserDto(course.getAuthor());
                courseDto.setAuthor(userDisplayDto);
                courseDto.setShortDescription(course.getShortDescription());

                if (photoBytes != null && photoBytes.length > 0) {
                    String base64Photo = Base64.encodeBase64String(photoBytes);
                    courseDto.setThumbnail(base64Photo);
                }

                result.add(courseDto);
            }

            return result; // Return plain List<CourseDto>
        } catch (Exception e) {
            throw new RuntimeException("Error fetching courses by category: " + e.getMessage());
        }
    }


    //Get all courses by category
//    @GetMapping("/{category}")
//    @QueryMapping
//    public ResponseEntity<?> getAllCoursesByCategory(@Argument String category){
//        try {
//            List<Course> courses = courseService.getAllCoursesByCategory(category);
//
//            List<CourseDto> result = new ArrayList<>();
//            for(Course course: courses ){
//
//                byte[] photoBytes = courseService.getThumbnailByCourseId(course.getId());
//                CourseDto courseDto = new CourseDto();
//                courseDto.setCourseId(course.getId());
//                courseDto.setCategoryId(course.getCategory().getId());
//                courseDto.setTitle(course.getTitle());
//                courseDto.setOriginalPrice(course.getOriginalPrice());
//                courseDto.setOfferPrice(course.getOfferPrice());
//
//                UserDisplayDto userDisplayDto = userServiceImpl.mapToUserDto(course.getAuthor());
//                courseDto.setAuthor(userDisplayDto);
//                courseDto.setShortDescription(course.getShortDescription());
//                //courseDto.setDescription(course.getDescription());
//                //courseDto.setLanguage(course.getLanguage());
//                //courseDto.setCreatedDate(course.getCreatedDate());
//                //courseDto.setVideos(course.getVideos());
//
//                if (photoBytes != null && photoBytes.length > 0) {
//                    String base64Photo = Base64.encodeBase64String(photoBytes);
//                    courseDto.setThumbnail(base64Photo);
//                }
//
//                result.add(courseDto);
//
//            }
//            return ResponseEntity.ok(result);
//        }catch (ItemNotFoundException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//        catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

    // Get a course by title
    @GetMapping("/course/{title:[a-zA-Z]+}")
    public ResponseEntity<?> getCourseByTitle(@PathVariable String title){
        try{
            Optional<Course> course = courseService.getCourseByTitle(title);

            byte[] photoBytes = courseService.getThumbnailByCourseId(course.get().getId());
            CourseDto courseDto = new CourseDto();
            courseDto.setId(course.get().getId());
            courseDto.setCategoryId(course.get().getCategory().getId());
            courseDto.setTitle(course.get().getTitle());

            UserDisplayDto userDisplayDto = userServiceImpl.mapToUserDto(course.get().getAuthor());
            courseDto.setAuthor(userDisplayDto);
            courseDto.setShortDescription(course.get().getShortDescription());
            courseDto.setDescription(course.get().getDescription());
            courseDto.setLanguage(course.get().getLanguage());
            courseDto.setOriginalPrice(course.get().getOriginalPrice());
            courseDto.setOfferPrice(course.get().getOfferPrice());
            courseDto.setCreatedDate(course.get().getCreatedDate());
            courseDto.setVideos(course.get().getVideos());

            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                courseDto.setThumbnail(base64Photo);
            }

            return ResponseEntity.ok(courseDto);
        }catch (ItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

//    @GetMapping("/course/{courseId:[0-9]+}")
    @QueryMapping
    public ResponseEntity<?> getCourseById(@Argument Long courseId){
        try{
            Optional<Course> course = courseService.getCourseById(courseId);

            byte[] photoBytes = courseService.getThumbnailByCourseId(courseId);
            CourseDto courseDto = new CourseDto();
            courseDto.setId(course.get().getId());
            courseDto.setCategoryId(course.get().getCategory().getId());
            courseDto.setTitle(course.get().getTitle());

            UserDisplayDto userDisplayDto = userServiceImpl.mapToUserDto(course.get().getAuthor());
            courseDto.setAuthor(userDisplayDto);
            courseDto.setShortDescription(course.get().getShortDescription());
            courseDto.setDescription(course.get().getDescription());
            courseDto.setOriginalPrice(course.get().getOriginalPrice());
            courseDto.setOfferPrice(course.get().getOfferPrice());
            courseDto.setLanguage(course.get().getLanguage());
            courseDto.setCreatedDate(course.get().getCreatedDate());
            courseDto.setVideos(course.get().getVideos());
            courseDto.setReviews(course.get().getReviews());

            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                courseDto.setThumbnail(base64Photo);
            }

            return ResponseEntity.ok(courseDto);
        }catch (ItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/viewed-courses")
    public ResponseEntity<List<CourseDto>> getRecentlyViewedCourses(@RequestParam("userId") Long userId){

        String key = "recently_viewed:" + userId;
        System.out.println("Fetching courses with key: " + key);
        List<CourseDto> recentlyViewedCourses = recentlyViewedCourseCache.opsForList().range(key, 0, -1);
        System.out.println("Recently viewed courses: " + recentlyViewedCourses);

        if (recentlyViewedCourses == null || recentlyViewedCourses.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no recently viewed courses found
        }

        return ResponseEntity.ok(recentlyViewedCourses);
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable Long courseId, @RequestBody CourseDto courseDto){
        try{
            Course theCourse = courseService.updateCourse(courseId, courseDto);
            UpdateResponseDto response = new UpdateResponseDto(
                    "Course updated successfully!",
                    theCourse.getId(),
                    theCourse.getTitle()
            );
            return ResponseEntity.ok(response);
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
