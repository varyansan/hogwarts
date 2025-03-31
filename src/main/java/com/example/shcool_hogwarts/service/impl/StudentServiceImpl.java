package com.example.shcool_hogwarts.service.impl;

import com.example.shcool_hogwarts.exception.NotFoundException;
import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;
import com.example.shcool_hogwarts.model.StudentProjection;
import com.example.shcool_hogwarts.repositories.FacultyRepository;
import com.example.shcool_hogwarts.repositories.StudentRepository;
import com.example.shcool_hogwarts.service.StudentService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentServiceImpl(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Student createStudent(Student student) {
        logger.info("Вызван метод createStudent");
        logger.debug("Создаваемый студент: {}", student);
        return studentRepository.save(student);
    }

    @Override
    public Student findStudent(long id) {
        logger.info("Вызван метод findStudent с id: {}", id);
        try {
            return studentRepository.findById(id).orElseThrow(() -> {
                logger.error("Студент с id {} не найден", id);
                return new NotFoundException(Student.class, id);
            });
        } catch (NotFoundException e) {
            logger.warn("Студент с id {} не найден", id, e);
            throw e;
        }
    }

    @Override
    public Student editStudent(Student student) {
        logger.info("Вызван метод editStudent с id: {}", student.getId());
        logger.debug("Изменяемый студент: {}", student);
        if (!studentRepository.existsById(student.getId())) {
            logger.error("Студент с id {} не найден", student.getId());
            throw new NotFoundException(Student.class, student.getId());
        }
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(long id) {
        logger.info("Вызван метод deleteStudent с id: {}", id);
        try {
            Student student = studentRepository.findById(id).orElseThrow(() -> {
                logger.error("Студент с id {} не найден", id);
                return new NotFoundException(Student.class, id);
            });
            studentRepository.deleteById(student.getId());
            logger.debug("Студент с id {} успешно удален", id);
        } catch (NotFoundException e) {
            logger.warn("Студент с id {} не найден", id, e);
            throw e;
        }
    }

    @Override
    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    @Override
    public List<Student> findAllStudent() {
        logger.info("Вызван метод findAllStudent");
        return studentRepository.findAll();
    }

    @Override
    public Faculty getFacultyByStudentId(Long id) {
        logger.info("Вызван метод getFacultyByStudentId с id: {}", id);
        try {
            return studentRepository.findById(id)
                    .map(Student::getFaculty)
                    .orElseThrow(() -> {
                        logger.error("Студент с id {} не найден", id);
                        return new NotFoundException(Student.class, id);
                    });
        } catch (NotFoundException e) {
            logger.warn("Студент с id {} не найден", id, e);
            throw e;
        }
    }

    @Override
    public Student assignFacultyToStudent(Long studentId, Long facultyId) {
        logger.info("Вызван метод assignFacultyToStudent, studentId: {}, facultyId: {}", studentId, facultyId);
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

    @Override
    public long countAllStudents() {
        logger.info("Вызван метод countAllStudents");
        return studentRepository.countAllStudents();
    }

    @Override
    public Double getAverageAge() {
        logger.info("Вызван метод getAverageAge");
        return studentRepository.getAverageAge();
    }

    @Transactional
    @Override
    public Page<StudentProjection> findLastFiveStudents() {
        logger.info("Вызван метод findLastFiveStudents");
        Pageable pageable = PageRequest.of(0, 5);
        return studentRepository.findLastFiveStudents(pageable);
    }

    @Override
    public void printParallel() {
        List<Student> students = studentRepository.findAll().stream()
                .limit(6)
                .toList();

        System.out.println(students.get(0));
        System.out.println(students.get(1));

        Thread parallell1 = new Thread(() -> {
            System.out.println(students.get(2));
            System.out.println(students.get(3));
        });

        Thread parallell2 = new Thread(() -> {
            System.out.println(students.get(4));
            System.out.println(students.get(5));
        });

        parallell1.start();
        parallell2.start();
    }

    @Override
    public void printSynchronized() {
        List<Student> listSynchronized = studentRepository.findAll().stream()
                .limit(6)
                .toList();

        System.out.println(listSynchronized.get(0));
        System.out.println(listSynchronized.get(1));

        Thread t1 = new Thread(() -> {
            printSynchronizedStudent(listSynchronized, 2, 3);
        });

        Thread t2 = new Thread(() -> {
            printSynchronizedStudent(listSynchronized, 4, 5);
        });

        t1.start();
        t2.start();
    }

    private void printSynchronizedStudent(List<Student> listSynchronized, int a, int b) {
        synchronized (this) {
            System.out.println(listSynchronized.get(a));
            System.out.println(listSynchronized.get(b));
        }
    }
}
