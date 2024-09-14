package com.edtech.EdTech.controller;

import com.edtech.EdTech.dto.VideoDto;
import com.edtech.EdTech.model.courses.Video;
import com.edtech.EdTech.service.LectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses/")
public class LectureController {

    private final LectureService lectureService;

    @PostMapping("/{courseId}/add-lecture")
    public ResponseEntity<?> addLecture(@Valid @PathVariable Long courseId, @RequestBody VideoDto videoDto){
        try{
            Video video = lectureService.saveLecture(courseId, videoDto);
            return ResponseEntity.ok(video);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
