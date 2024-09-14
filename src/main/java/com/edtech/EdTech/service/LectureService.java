package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.VideoDto;
import com.edtech.EdTech.model.courses.Video;

public interface LectureService {

    Video saveLecture(Long courseId, VideoDto videoDto);

}
