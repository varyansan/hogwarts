package com.example.shcool_hogwarts.TestRestTemplate;

import com.example.shcool_hogwarts.controller.StudentController;
import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;
import com.example.shcool_hogwarts.service.impl.StudentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;


import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentController studentController;

    @MockBean
    private StudentServiceImpl studentService;

    private Student student;
    private Faculty faculty;

    @BeforeEach
    public void setUp() {
        long studentId = 1L;
        student = new Student();
        student.setId(studentId);
        student.setName("Oleg");
        student.setAge(18);
        student.setFaculty(faculty);

        long facultyId = 1L;
        faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName("Java");
        faculty.setColor("Black");
    }

    @Test
    @DisplayName("Контроллер запускается, не Null")
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    @DisplayName("URL /students возвращает значения")
    public void testGetStudents() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students", String.class))
                .isNotNull();

    }

    @Test
    @DisplayName("Тест на поиск студента по ID")
    public void getFindStudentTest() {
        long studentId = 1L;

        when(studentService.findStudent(studentId)).thenReturn(student);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + studentId + "/find-student",
                Student.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(studentId);
        assertThat(response.getBody().getName()).isEqualTo("Oleg");
        assertThat(response.getBody().getAge()).isEqualTo(18);
    }

    @Test
    @DisplayName("Тест на добавление студента")
    public void createStudentTest() throws Exception {
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        String studentJson = objectMapper.writeValueAsString(student);

        HttpEntity<String> request = new HttpEntity<>(studentJson, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/add-student",
                HttpMethod.POST,
                request,
                Student.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(student.getId());
        assertThat(response.getBody().getName()).isEqualTo(student.getName());
        assertThat(response.getBody().getAge()).isEqualTo(student.getAge());
    }

    @Test
    @DisplayName("Тест на обновление студента")
    public void editStudentTest() throws Exception {
        long studentId = 1L;

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setName("UpdatedOleg");
        updatedStudent.setAge(19);

        when(studentService.editStudent(any(Student.class))).thenReturn(updatedStudent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        String studentJson = objectMapper.writeValueAsString(updatedStudent);

        HttpEntity<String> request = new HttpEntity<>(studentJson, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/" + studentId + "/edite",
                HttpMethod.PUT,
                request,
                Student.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(studentId);
        assertThat(response.getBody().getName()).isEqualTo(updatedStudent.getName());
        assertThat(response.getBody().getAge()).isEqualTo(updatedStudent.getAge());
    }

    @Test
    @DisplayName("Тест на удаление студента")
    public void deleteStudentTest() {
        long studentId = 1L;

        doNothing().when(studentService).deleteStudent(studentId);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/" + studentId + "/delete-student",
                HttpMethod.DELETE,
                null,
                Void.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);

        verify(studentService).deleteStudent(studentId);
    }

    @Test
    @DisplayName("Тест на поиск студентов по возрасту")
    public void getStudentsByAgeTest() {
        int minAge = 15;
        int maxAge = 20;

        Student student1 = new Student();
        student1.setId(student.getId());
        student1.setName(student.getName());
        student1.setAge(student.getAge());

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Nikita");
        student2.setAge(16);

        Student student3 = new Student();
        student3.setId(3L);
        student3.setName("CryDuck");
        student3.setAge(22);

        List<Student> students = Arrays.asList(student1, student2);

        when(studentService.findByAgeBetween(minAge, maxAge)).thenReturn(students);

        ResponseEntity<List> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/by-age?minAge=" + minAge + "&maxAge=" + maxAge,
                HttpMethod.GET,
                null,
                List.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Тест на получение факультета студента по ID")
    public void getFacultyByStudentIdTest() {
        long studentId = 1L;

        when(studentService.getFacultyByStudentId(studentId)).thenReturn(faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + studentId + "/faculty-by-student",
                Faculty.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
        assertThat(response.getBody().getColor()).isEqualTo(faculty.getColor());
    }

    @Test
    @DisplayName("Тест на назначение факультета студенту")
    public void assignFacultyToStudentTest() {
        long studentId = 1L;
        long facultyId = 1L;

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setName("Oleg");
        updatedStudent.setAge(18);
        updatedStudent.setFaculty(faculty);

        when(studentService.assignFacultyToStudent(studentId, facultyId)).thenReturn(updatedStudent);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/" + studentId + "/faculty/" + facultyId,
                HttpMethod.PUT,
                null,
                Void.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);

        verify(studentService).assignFacultyToStudent(studentId, facultyId);
    }
}
