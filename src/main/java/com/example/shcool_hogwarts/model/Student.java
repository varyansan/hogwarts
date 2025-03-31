package com.example.shcool_hogwarts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id;

    private String name;
    private int age;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    @JsonIgnore
    private Faculty faculty;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    private Avatar avatar;
}
