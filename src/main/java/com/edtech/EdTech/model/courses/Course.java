package com.edtech.EdTech.model.courses;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
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

    @Column(name = "course_author", nullable = false)
    @Size(max=50)
    private String author;

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
    private byte[] thumbnail;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos;

    public Course(){
        this.createdDate = LocalDate.now();
    }

}
