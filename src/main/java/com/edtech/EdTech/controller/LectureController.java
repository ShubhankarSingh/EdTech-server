package com.edtech.EdTech.controller;

import com.edtech.EdTech.dto.VideoDto;
import com.edtech.EdTech.model.courses.Video;
import com.edtech.EdTech.repository.LectureRespository;
import com.edtech.EdTech.service.LectureService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses/")
public class LectureController {

    private final LectureService lectureService;
    private final LectureRespository lectureRespository;

    @Value("${project.video}")
    private String path;

    @PostMapping("/{courseId}/add-lecture")
    public ResponseEntity<?> addLecture(@Valid @PathVariable Long courseId,
                                        @RequestParam("title") String title,
                                        @RequestParam("url") MultipartFile file){
        try{

            String fileName = saveFile(file);

            VideoDto videoDto = new VideoDto();
            videoDto.setTitle(title);
            videoDto.setUrl(fileName);  // Relative URL

            Video video = lectureService.saveLecture(courseId, videoDto);
            return ResponseEntity.ok(video);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private String saveFile(MultipartFile file) throws IOException {

        String uploadDir = "uploads/videos";
        String fileName = file.getOriginalFilename();
        String fileExtension = "";

        if(fileName != null){
            fileExtension = fileName.substring(fileName.lastIndexOf("."));
        }

        String randomChars = UUID.randomUUID().toString().substring(0,8);
        String newFileName = fileName.replace(fileExtension, "") + "_" + randomChars + System.currentTimeMillis() + fileExtension;

        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        try(InputStream inputStream = file.getInputStream()){
            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(inputStream, filePath);
        }catch (IOException e){
            throw new IOException("Could not save file: " + fileName, e);
        }

        return newFileName;
    }


    @GetMapping("/play/{videoId}")
    public void playLecture(@PathVariable Long videoId, HttpServletResponse response) throws IOException{
        System.out.println("Hello");
        Optional<Video> video = lectureRespository.findById(videoId);
        InputStream resource = lectureService.getResource(path, video.get().getUrl(), videoId);
        response.setContentType(MediaType.ALL_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }

    @GetMapping("/{courseId}/all-lectures")
    public ResponseEntity<?> getAllLectures(@PathVariable Long courseId){

        try{
            List<Video> lectures = lectureService.getAllLectures(courseId);
            return ResponseEntity.ok(lectures);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
