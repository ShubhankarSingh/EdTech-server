package com.edtech.EdTech.service;

import com.edtech.EdTech.model.courses.Review;

import java.sql.SQLException;
import java.util.List;

public interface ReviewService {

    Review addReview(Review review) throws SQLException;

    String deleteReview(Long reviewId);

//    List<Review> getAllReviews(Long courseId);
}
