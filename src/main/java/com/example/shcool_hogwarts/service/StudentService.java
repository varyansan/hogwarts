package com.example.shcool_hogwarts.service;

import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;

import java.util.Collection;

public interface StudentService {
    Student createStudent(Student student);

    Student findStudent(long id);

    Student editStudent(Student student);

    void deleteStudent(long id);

    Collection<Student> findByAgeBetween(int minAge, int maxAge);

    Collection<Student> findAllStudent();

    Faculty getFacultyByStudentId(Long studentId);

    Student assignFacultyToStudent(Long studentId, Long facultyId);
}
