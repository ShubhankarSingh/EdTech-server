package com.edtech.EdTech.model.users;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "role_name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role(String roleName) {
        this.name = roleName;
    }
}
