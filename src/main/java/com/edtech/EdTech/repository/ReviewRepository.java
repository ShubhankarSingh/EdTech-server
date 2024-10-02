package com.edtech.EdTech.repository;

import com.edtech.EdTech.model.courses.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
