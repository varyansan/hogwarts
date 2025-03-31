package com.example.shcool_hogwarts.service.impl;

import com.example.shcool_hogwarts.exception.NotFoundException;
import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;
import com.example.shcool_hogwarts.repositories.FacultyRepository;
import com.example.shcool_hogwarts.repositories.StudentRepository;
import com.example.shcool_hogwarts.service.FacultyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Create faculty");
        logger.debug("Faculty: {}", faculty);
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFaculty(Long id) {
        logger.info("Find faculty by id: {}", id);
        return facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("Факультет с id {} не найден", id);
            return new NotFoundException(Faculty.class, id);
        });
    }

    @Override
    public void deleteFaculty(long id) {
        logger.info("Delete faculty by id: {}", id);
        facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("Факультет с id {} не найден", id);
            return new NotFoundException(Faculty.class, id);
        });
        facultyRepository.deleteById(id);
        logger.debug("Faculty {} deleted", id);
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        logger.info("Edit faculty");
        logger.debug("Faculty: {}", faculty);
        long id = faculty.getId();
        Faculty existingFaculty = facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("Факультет с id {} не найден", id);
            return new NotFoundException(Faculty.class, id);
        });
        existingFaculty.setName(faculty.getName());
        existingFaculty.setColor(faculty.getColor());
        return facultyRepository.save(existingFaculty);
    }

    @Override
    public List<Faculty> findByColor(String color) {
        logger.info("Find faculty by color: {}", color);
        return facultyRepository.findByColorContainingIgnoreCase(color);
    }

    @Override
    public List<Faculty> findAllFaculty() {
        logger.info("Find faculty");
        return facultyRepository.findAll();
    }

    @Override
    public List<Student> getStudentsByFaculty(long id) {
        logger.info("Get students by id: {}", id);
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Not found students by faculty by id: {}", id);
                    return new NotFoundException(Faculty.class, id);
                });
        return studentRepository.findAllByFaculty(faculty);
    }
}
