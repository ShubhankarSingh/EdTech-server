package com.edtech.EdTech.repository;

import com.edtech.EdTech.model.courses.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {


}
