package com.example.shcool_hogwarts.TestRestTemplate;

import com.example.shcool_hogwarts.controller.FacultyController;
import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;
import com.example.shcool_hogwarts.service.impl.FacultyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.AbstractBooleanAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyController facultyController;

    @MockBean
    private FacultyServiceImpl facultyService;

    private Faculty faculty;
    private Student student;

    @BeforeEach
    public void setUp() {
        long facultyId = 1L;
        String facultyName = "Диплом";
        String color = "Black";
        faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(color);
    }

    @Test
    @DisplayName("Контроллер запускается, не null")
    void contextLoads() {
        assertThat(facultyController).isNotNull();
    }

    @Test
    @DisplayName("Факультет возвращает значения")
    public void testGetFaculty() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty", Student.class))
                .isNotNull();
    }

    @Test
    @DisplayName("Тест на поиск факультета по ID")
    void getFindFacultyTest() throws Exception {
        long facultyId = 1L;

        when(facultyService.findFaculty(facultyId)).thenReturn(faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + facultyId + "/find-faculty", Faculty.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Диплом");
        assertThat(response.getBody().getColor()).isEqualTo("Black");
    }

    @Test
    void createFacultyTest() throws Exception {
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        String facultyJson = objectMapper.writeValueAsString(faculty);

        HttpEntity<String> request = new HttpEntity<>(facultyJson, headers);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/add-faculty",
                HttpMethod.POST,
                request,
                Faculty.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
        assertThat(response.getBody().getColor()).isEqualTo(faculty.getColor());
        assertThat(response.getBody().getId()).isEqualTo(faculty.getId());
    }

    @Test
    @DisplayName("Тест на обновление факультета")
    void editFacultyTest() throws Exception {
        long facultyId = 1L;

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(facultyId);
        updatedFaculty.setName("DuckFaculty");
        updatedFaculty.setColor("White");

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(updatedFaculty);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        String facultyJson = objectMapper.writeValueAsString(updatedFaculty);

        HttpEntity<String> request = new HttpEntity<>(facultyJson, headers);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + facultyId + "/update",
                HttpMethod.PUT,
                request,
                Faculty.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("DuckFaculty");
        assertThat(response.getBody().getColor()).isEqualTo("White");
    }

    @Test
    @DisplayName("Тест на удаление факультета")
    public void deleteFacultyTest() {
        long facultyId = 1L;

        doNothing().when(facultyService).deleteFaculty(facultyId);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + facultyId + "/delete",
                HttpMethod.DELETE,
                null,
                Void.class);

        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getBody()).isNull();

        verify(facultyService).deleteFaculty(facultyId);
    }


    @Test
    @DisplayName("Тест на фильтрацию факультетов по цвету")
    public void filterColorTest() {
        String paramColor = "Black";

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("C++");
        faculty2.setColor("White");

        List<Faculty> faculties = Arrays.asList(faculty);

        when(facultyService.findByColor(paramColor)).thenReturn(faculties);

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/color?color=" + paramColor,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                });

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().get(0).getColor()).isEqualTo(paramColor);
    }


    @Test
    @DisplayName("Тест на получение студентов по ID факультета")
    void getStudentsByFaculty() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("TurboDuck");
        student1.setAge(18);
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("LolDuck");
        student2.setAge(19);
        student2.setFaculty(faculty);

        long facultyId = 1L;

        List<Student> students = Arrays.asList(student1, student2);

        when(facultyService.getStudentsByFaculty(facultyId)).thenReturn(students);

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + facultyId + "/students-faculty",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                });

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        AbstractBooleanAssert<?> abstractBooleanAssert = assertThat(response.getBody().containsAll(students));
    }
}
