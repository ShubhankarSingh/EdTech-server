package com.edtech.EdTech.controller;

import com.edtech.EdTech.model.courses.Review;
import com.edtech.EdTech.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add-review")
    public ResponseEntity<Review> addReview(@RequestBody Review review){

        Review theReview = reviewService.addReview(review);

        return ResponseEntity.ok(theReview);
    }

}
