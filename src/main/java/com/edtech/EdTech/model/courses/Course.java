package com.edtech.EdTech.model.courses;

import jakarta.persistence.*;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String shortDescription;
    private String description;
    private String language;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
