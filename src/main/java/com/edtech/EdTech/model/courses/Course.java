package com.edtech.EdTech.model.courses;

import com.edtech.EdTech.model.users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "courses")
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_title", nullable = false)
    @Size(min=3, max=100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User author;

    @Size(max=200)
    private String shortDescription;

    @Column(name = "course_description", nullable = false)
    private String description;

    @Column(name = "course_language", length = 20)
    private String language;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Lob
    @Column(name = "thumbnail")
    private Blob thumbnail;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos;

    public Course(){
        this.createdDate = LocalDate.now();
    }

}
