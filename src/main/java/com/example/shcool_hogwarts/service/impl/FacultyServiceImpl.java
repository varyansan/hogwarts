package com.example.shcool_hogwarts.service.impl;

import com.example.shcool_hogwarts.exception.NotFoundException;
import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;
import com.example.shcool_hogwarts.repositories.FacultyRepository;
import com.example.shcool_hogwarts.repositories.StudentRepository;
import com.example.shcool_hogwarts.service.FacultyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Faculty.class, id));
    }

    @Override
    public void deleteFaculty(long id) {
        facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Faculty.class, id));
        facultyRepository.deleteById(id);
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        long id = faculty.getId();
        Faculty existingFaculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Faculty.class, id));
        existingFaculty.setName(faculty.getName());
        existingFaculty.setColor(faculty.getColor());
        return facultyRepository.save(existingFaculty);
    }

    @Override
    public List<Faculty> findByColor(String color) {
        return facultyRepository.findByColorContainingIgnoreCase(color);
    }

    @Override
    public List<Faculty> findAllFaculty() {
        return facultyRepository.findAll();
    }

    @Override
    public List<Student> getStudentsByFaculty(long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Faculty.class, id));
        return studentRepository.findAllByFaculty(faculty);
    }
}
