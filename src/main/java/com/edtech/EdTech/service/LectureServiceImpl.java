package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.VideoDto;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.courses.Video;
import com.edtech.EdTech.repository.CourseRepository;
import com.edtech.EdTech.repository.LectureRespository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

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

    @Override
    public List<Video> getAllLectures(Long courseId) {
        Course theCourse = courseRepository.findById(courseId)
                .orElseThrow(()-> new RuntimeException("No course found!"));
        List<Video> lectures = theCourse.getVideos();
        return lectures;
    }

    @Override
    public InputStream getResource(String path, String fileName, Long videoId) throws FileNotFoundException {
        String fullPath = path+File.separator+fileName;
        System.out.println("/n/n" + fullPath + "/n/n/n");
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream ;
    }

    @Override
    public Video updateLecture(Course theCourse, Video theVideo, VideoDto videoDto) {

        theVideo.setTitle(videoDto.getTitle());
        theVideo.setUrl(videoDto.getUrl());

        return lectureRespository.save(theVideo);
    }


}
