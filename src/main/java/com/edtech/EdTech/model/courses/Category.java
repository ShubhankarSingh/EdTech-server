package com.edtech.EdTech.model.courses;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoryType;

    @OneToMany(mappedBy = "category")
    private Set<Course> courses;

}
