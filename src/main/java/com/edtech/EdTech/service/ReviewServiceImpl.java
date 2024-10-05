package com.edtech.EdTech.service;

import com.edtech.EdTech.exception.ItemNotFoundException;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.courses.Review;
import com.edtech.EdTech.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final CourseService courseService;
    private final ReviewRepository reviewRepository;
    @Override
    public Review addReview(Review review) {

        Review newReview = new Review();
        newReview.setDescription(review.getDescription());
        newReview.setRating(review.getRating());
        newReview.setUserId(review.getUserId());
        newReview.setUsername(review.getUsername());
        newReview.setTimestamp(review.getTimestamp());

        Optional<Course> theCourse = courseService.getCourseById(review.getCourseId());
        if(theCourse.isPresent()){
            newReview.setCourse(theCourse.get());
        }else {
            throw new ItemNotFoundException("Course not found");
        }

        reviewRepository.save(newReview);
        return newReview;
    }

//    @Override
//    public List<Review> getAllReviews(Long courseId) {
//        return reviewRepository.findByCourseId(courseId);
//    }
}
