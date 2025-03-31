package com.example.shcool_hogwarts.service.impl;

import com.example.shcool_hogwarts.exception.NotFoundException;
import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;
import com.example.shcool_hogwarts.repositories.FacultyRepository;
import com.example.shcool_hogwarts.repositories.StudentRepository;
import com.example.shcool_hogwarts.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentServiceImpl(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student findStudent(long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Student.class, id));
    }

    @Override
    public Student editStudent(Student student) {
        if (!studentRepository.existsById(student.getId())) {
            throw new NotFoundException(Student.class, student.getId());
        }
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Student.class, id));
        studentRepository.deleteById(student.getId());
    }

    @Override
    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    @Override
    public List<Student> findAllStudent() {
        return studentRepository.findAll();
    }

    @Override
    public Faculty getFacultyByStudentId(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(Student::getFaculty)
                .orElseThrow(() -> new NotFoundException(Student.class, id));
    }

    @Override
    public Student assignFacultyToStudent(Long studentId, Long facultyId) {
        Optional<Student> student = studentRepository.findById(studentId);
        Optional<Faculty> faculty = facultyRepository.findById(facultyId);

        if (student.isPresent() && faculty.isPresent()) {
            student.get().setFaculty(faculty.get());
            return studentRepository.save(student.get());
        } else {
            try {
                throw new ClassNotFoundException("Студент или факультет не найден");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
