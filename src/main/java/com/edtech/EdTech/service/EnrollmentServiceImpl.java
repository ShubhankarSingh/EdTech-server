package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.*;
import com.edtech.EdTech.exception.ItemNotFoundException;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.courses.Enrollment;
import com.edtech.EdTech.model.users.User;
import com.edtech.EdTech.repository.CourseRepository;
import com.edtech.EdTech.repository.EnrollmentRepository;
import com.edtech.EdTech.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class EnrollmentServiceImpl implements EnrollmentService{

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;
    private final CourseService courseService;

    @Autowired
    private RedisTemplate<String, List<EnrollmentDto>> enrollmentCacheDtoRedisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public void enrollCourse(Long userId, Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(()-> new ItemNotFoundException("Course not found with id: " + courseId));

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ItemNotFoundException("User not found with id: " + userId));

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setUser(user);
        enrollment.setEnrollmentDate(LocalDate.now());

        enrollmentRepository.save(enrollment);

        // Invalidate cache after enrolling to ensure it reflects the latest data
        String redisKey = "userEnrollment:" + userId;
        enrollmentCacheDtoRedisTemplate.delete(redisKey);
    }

    public List<EnrollmentDto> getEnrolledCourses(Long userId) throws SQLException {

        String redisKey = "userEnrollment:" + userId;
        List<EnrollmentDto> cachedEnrolledCourses = enrollmentCacheDtoRedisTemplate.opsForValue().get(redisKey);
        if(cachedEnrolledCourses != null && !cachedEnrolledCourses.isEmpty()){
            logger.info("\n\nCache hit for enrolled courses for userId: {}\n", userId);
            return cachedEnrolledCourses;
        }

        logger.info("\n\nCache miss for enrolled courses with userId: {}. Fetching from database...\n\n", userId); // Log when fetching from DB
        // If not cached, retrieve from the database
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ItemNotFoundException("User not found with id: " + userId));

        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);

        List<EnrollmentDto> result = mapToEnrollmentDto(enrollments);

        // Cache the list of EnrollmentCacheDto objects
        enrollmentCacheDtoRedisTemplate.opsForValue().set(redisKey, result, Duration.ofMinutes(10));

        return result;
    }

    private List<EnrollmentDto> mapToEnrollmentDto(List<Enrollment> enrollments) throws SQLException {
        List<EnrollmentDto> result = new ArrayList<>();
        for(Enrollment enrollment: enrollments){

            EnrollmentDto enrollmentDto = new EnrollmentDto();
            enrollmentDto.setEnrollmentId(enrollment.getEnrollmentId());
            enrollmentDto.setEnrollmentDate(enrollment.getEnrollmentDate());

            byte[] photoBytes = courseService.getThumbnailByCourseId(enrollment.getCourse().getId());
            CourseDto courseDto = new CourseDto();
            courseDto.setCourseId(enrollment.getCourse().getId());
            courseDto.setTitle(enrollment.getCourse().getTitle());

            UserDisplayDto userDisplayDto = mapToUserDto(enrollment.getCourse().getAuthor());
            courseDto.setAuthor(userDisplayDto);

            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                courseDto.setThumbnail(base64Photo);
            }

            enrollmentDto.setCourse(courseDto);
            result.add(enrollmentDto);
        }
        return result;
    }

    public UserDisplayDto mapToUserDto(User user) {
        UserDisplayDto userDto = new UserDisplayDto();
        userDto.setName(user.getName());
        return userDto;
    }

    @Override
    public boolean checkEnrollmentStatus(Long userId, Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(()-> new ItemNotFoundException("Course not found with id: " + courseId));

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ItemNotFoundException("User not found with id: " + userId));

        return enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
    }
}
