package com.edtech.EdTech.service;

import com.edtech.EdTech.exception.ItemNotFoundException;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.courses.Enrollment;
import com.edtech.EdTech.model.users.User;
import com.edtech.EdTech.repository.CourseRepository;
import com.edtech.EdTech.repository.EnrollmentRepository;
import com.edtech.EdTech.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class EnrollmentServiceImpl implements EnrollmentService{

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

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
    }

    @Override
    public List<Enrollment> getEnrolledCourses(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ItemNotFoundException("User not found with id: " + userId));

        return enrollmentRepository.findByUserId(userId);
    }

    @Override
    public boolean checkEnrollmentStatus(Long userId, Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(()-> new ItemNotFoundException("Course not found with id: " + courseId));

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ItemNotFoundException("User not found with id: " + userId));

        boolean isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
        return isEnrolled;
    }
}
