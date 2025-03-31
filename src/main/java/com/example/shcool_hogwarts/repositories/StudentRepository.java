package com.example.shcool_hogwarts.repositories;

import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;
import com.example.shcool_hogwarts.model.StudentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findAllByFaculty(Faculty faculty);

    @Query(value = "SELECT COUNT(*) FROM Student", nativeQuery = true)
    long countAllStudents();

    @Query(value = "SELECT AVG(age) FROM Student", nativeQuery = true)
    Double getAverageAge();

    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    Page<StudentProjection> findLastFiveStudents(Pageable pageable);
}
