package com.example.shcool_hogwarts.repositories;

import com.example.shcool_hogwarts.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> findByColorContainingIgnoreCase(String color);
}