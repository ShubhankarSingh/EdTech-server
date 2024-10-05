package com.edtech.EdTech.controller;

import com.edtech.EdTech.exception.ItemNotFoundException;
import com.edtech.EdTech.model.courses.Review;
import com.edtech.EdTech.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add-review")
    public ResponseEntity<?> addReview(@Valid @RequestBody Review review){
        try {
            Review theReview = reviewService.addReview(review);
            return ResponseEntity.ok(theReview);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing the review: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId){

        try{
            String message = reviewService.deleteReview(reviewId);
            return ResponseEntity.ok(message);
        }catch (ItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

//    @GetMapping("/{courseId}")
//    public ResponseEntity<?> getAllReviews(@RequestParam("courseId") Long courseId){
//        try{
//            List<Review> reviews = reviewService.getAllReviews(courseId);
//            return ResponseEntity.ok(reviews);
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

}
