package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.CourseDto;
import com.edtech.EdTech.exception.ItemNotFoundException;
import com.edtech.EdTech.model.courses.Category;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.users.User;
import com.edtech.EdTech.repository.CategoryRepository;
import com.edtech.EdTech.repository.CourseRepository;
import com.edtech.EdTech.repository.UserRepository;
import com.edtech.EdTech.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    private RedisTemplate<String, CourseDto> recentlyViewedCourseCache;
    @Override
    public Course addNewCourse(CourseDto courseDto, MultipartFile thumbnail) throws IOException, SQLException {

        User theUser = userRepository.findById(courseDto.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Course theCourse = new Course();
        theCourse.setAuthor(theUser);
        theCourse.setTitle(courseDto.getTitle());
        theCourse.setShortDescription(courseDto.getShortDescription());
        theCourse.setDescription(courseDto.getDescription());
        theCourse.setOriginalPrice(courseDto.getOriginalPrice());
        theCourse.setOfferPrice(courseDto.getOfferPrice());
        theCourse.setLanguage(courseDto.getLanguage());
        theCourse.setCreatedDate(courseDto.getCreatedDate());

        Category category = categoryRepository.findById(courseDto.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found with ID: " + courseDto.getCategoryId()));
        theCourse.setCategory(category);

        if(!thumbnail.isEmpty()){
            byte[] photo = thumbnail.getBytes();
            Blob photoBlob = new SerialBlob(photo);
            theCourse.setThumbnail(photoBlob);
        }

        return courseRepository.save(theCourse);
    }

    @Override
    public List<Course> getAllCourses() {
        return null;
    }

    @Override
    public List<Course> getAllCoursesByCategory(String category) {
        Category theCategory = categoryRepository.findByCategoryType(category);
        if(theCategory == null){
            throw new ItemNotFoundException("We can’t find the page looking for.");
        }
        List<Course> courses = courseRepository.findCoursesByCategoryId(theCategory.getId());

        return courses;
    }

    @Override
    public byte[] getThumbnailByCourseId(Long courseId) throws SQLException {
        Course theCourse = courseRepository.findById(courseId)
                .orElseThrow(()-> new ItemNotFoundException("No course found with id: " + courseId));

        Blob photoBlob = theCourse.getThumbnail();
        if(photoBlob != null){
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Override
    public Optional<Course> getCourseById(Long courseId) throws SQLException {
        Optional<Course> theCourse = courseRepository.findById(courseId);
        if (theCourse.isEmpty()) {
            throw new ItemNotFoundException("We can’t find the page you’re looking for.");
        }

        // Add recently viewed courses to Redis
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("\n\n\n User auth:" + authentication);
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();

            System.out.println("\n\n\n User id is:" + userId);
            String key = "recently_viewed:" + userId;

            // Check if the course is already in the Redis list
            List<CourseDto> existingCourses = recentlyViewedCourseCache.opsForList().range(key, 0, -1);
            boolean alreadyExists = existingCourses!=null && existingCourses.stream()
                    .anyMatch(course -> course.getCourseId().equals(courseId));

            if(!alreadyExists) {

                // Proceed with storing course in Redis for this authenticated user
                CourseDto viewedCourse = new CourseDto();
                viewedCourse.setCourseId(courseId);
                viewedCourse.setTitle(theCourse.get().getTitle());

                byte[] photoBytes = getThumbnailByCourseId(courseId);
                if (photoBytes != null && photoBytes.length > 0) {
                    String base64Photo = Base64.encodeBase64String(photoBytes);
                    viewedCourse.setThumbnail(base64Photo);
                }

                //Push the CourseDto directly to the Redis list and trim to keep only the top 3 items
                recentlyViewedCourseCache.opsForList().leftPush(key, viewedCourse);
                recentlyViewedCourseCache.opsForList().trim(key, 0, 2);

                // Set TTL (Time-To-Live) for the key
                recentlyViewedCourseCache.expire(key, Duration.ofHours(1));

                System.out.println("Course added to Redis list.");
            } else {
                System.out.println("Course already exists in Redis list. Skipping addition.");
            }
        }

        return theCourse;
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

//            if(courseDto.getAuthor() != null) theCourse.setAuthor(courseDto.getAuthor());
            if(courseDto.getTitle() != null) theCourse.setTitle(courseDto.getTitle());
            if(courseDto.getDescription() != null) theCourse.setDescription(courseDto.getDescription());
            if(courseDto.getShortDescription() != null) theCourse.setShortDescription(courseDto.getShortDescription());
            if(courseDto.getLanguage() != null) theCourse.setLanguage(courseDto.getLanguage());
            if(courseDto.getOriginalPrice() != null) theCourse.setOriginalPrice(courseDto.getOriginalPrice());
            if(courseDto.getOfferPrice() != null) theCourse.setOfferPrice(courseDto.getOfferPrice());
//            if(courseDto.getCreatedDate() != null) theCourse.setCreatedDate(courseDto.getCreatedDate());
//
//            Category category = categoryRepository.findById(courseDto.getCategoryId())
//                    .orElseThrow(()-> new RuntimeException("Category not found with ID: " + courseDto.getCategoryId()));
//            theCourse.setCategory(category);
            return courseRepository.save(theCourse);
        }catch (Exception e){
            throw new RuntimeException("An error occurred while saving the course: " + e.getMessage());
        }
    }
}
