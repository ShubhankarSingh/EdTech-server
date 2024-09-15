package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.VideoDto;
import com.edtech.EdTech.model.courses.Video;

import java.util.List;

public interface LectureService {

    Video saveLecture(Long courseId, VideoDto videoDto);

    List<Video> getAllLectures();

}
