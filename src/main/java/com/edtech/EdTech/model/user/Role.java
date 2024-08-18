package com.edtech.EdTech.model.user;


import jakarta.persistence.*;
import lombok.Generated;

import java.util.List;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "role_name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

}
