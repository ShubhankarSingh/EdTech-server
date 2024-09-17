package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.VideoDto;
import com.edtech.EdTech.model.courses.Video;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface LectureService {

    Video saveLecture(Long courseId, VideoDto videoDto);

    List<Video> getAllLectures(Long courseId);

    InputStream getResource(String path,String fileName , Long videoId) throws FileNotFoundException ;
}
