package com.edtech.EdTech.dto;

import com.edtech.EdTech.model.courses.Category;
import com.edtech.EdTech.model.courses.Video;
import com.edtech.EdTech.model.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private Long courseId;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Author is required")
    private Long userId;

    private UserDisplayDto author;

    @NotEmpty(message = "Short Description is required")
    private String shortDescription;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotEmpty(message = "Language is required")
    private String language;

    @NotNull(message = "Created Date is required")
    private LocalDate createdDate;

    @NotNull(message = "Category is required")
    private Long categoryId;

    // Field for the thumbnail image
    private String thumbnail;

    // List of video URLs or video DTOs
    private List<Video> videos;

}
