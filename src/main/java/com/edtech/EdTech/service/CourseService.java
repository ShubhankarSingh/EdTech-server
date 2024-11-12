package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.CourseDto;
import com.edtech.EdTech.model.courses.Category;
import com.edtech.EdTech.model.courses.Course;
import org.hibernate.annotations.processing.SQL;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CourseService {

    Course addNewCourse(CourseDto courseDto, MultipartFile thumbnail) throws IOException, SQLException;
    List<Course> getAllCourses();
    List<Course> getAllCoursesByCategory(String category);
    byte[] getThumbnailByCourseId(Long courseId) throws SQLException;
    Optional<Course> getCourseById(Long id) throws SQLException;

    Optional<Course> getCourseByTitle(String title);

    void deleteCourse(Long courseId);

    Course updateCourse(Long courseId, CourseDto courseDto);
}
