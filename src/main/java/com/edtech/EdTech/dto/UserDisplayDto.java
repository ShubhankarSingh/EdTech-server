package com.edtech.EdTech.dto;

import com.edtech.EdTech.model.users.Role;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDisplayDto {

    //private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String email;
    private String profilePicture;
    private List<CourseDto> courses;

}
