package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.CourseDto;
import com.edtech.EdTech.exception.ItemNotFoundException;
import com.edtech.EdTech.model.courses.Category;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.users.User;
import com.edtech.EdTech.repository.CategoryRepository;
import com.edtech.EdTech.repository.CourseRepository;
import com.edtech.EdTech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Course addNewCourse(CourseDto courseDto, MultipartFile thumbnail) throws IOException, SQLException {


        User theUser = userRepository.findById(courseDto.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Course theCourse = new Course();
        theCourse.setAuthor(theUser);
        theCourse.setTitle(courseDto.getTitle());
        theCourse.setShortDescription(courseDto.getShortDescription());
        theCourse.setDescription(courseDto.getDescription());
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
            throw new ItemNotFoundException("We can’t find the page  looking for.");
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
    public Optional<Course> getCourseById(Long id) {
        Optional<Course> theCourse = courseRepository.findById(id);
        if(theCourse.isEmpty()){
            throw new ItemNotFoundException("We can’t find the page you’re looking for.");
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
            if(courseDto.getCreatedDate() != null) theCourse.setCreatedDate(courseDto.getCreatedDate());

            Category category = categoryRepository.findById(courseDto.getCategoryId())
                    .orElseThrow(()-> new RuntimeException("Category not found with ID: " + courseDto.getCategoryId()));
            theCourse.setCategory(category);

            return courseRepository.save(theCourse);
        }catch (Exception e){
            throw new RuntimeException("An error occurred while saving the course: " + e.getMessage());
        }
    }
}
