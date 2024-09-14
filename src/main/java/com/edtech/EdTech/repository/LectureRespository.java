package com.edtech.EdTech.repository;

import com.edtech.EdTech.model.courses.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRespository extends JpaRepository<Video, Long> {
}
