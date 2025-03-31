package com.example.shcool_hogwarts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "avatar")
public class Avatar {

    @Id
    @GeneratedValue
    private Long id;

    private String filePath;
    private long fileSize;

    private String mediaType;

    @Lob
    @JsonIgnore
    private byte[] data;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private Student student;
}
