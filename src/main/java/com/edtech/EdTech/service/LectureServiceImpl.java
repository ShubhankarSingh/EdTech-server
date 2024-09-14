package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.VideoDto;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.courses.Video;
import com.edtech.EdTech.repository.CourseRepository;
import com.edtech.EdTech.repository.LectureRespository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService{

    private final LectureRespository lectureRespository;
    private final CourseRepository courseRepository;

    @Override
    public Video saveLecture(Long courseId, VideoDto videoDto) {
        Video video = new Video();
        video.setTitle(videoDto.getTitle());
        video.setUrl(videoDto.getUrl());

        Course theCourse = courseRepository.findById(courseId)
                .orElseThrow(()-> new RuntimeException("No course found, Please go to your course to add lectures"));

        video.setCourse(theCourse);
        return lectureRespository.save(video);
    }
}
