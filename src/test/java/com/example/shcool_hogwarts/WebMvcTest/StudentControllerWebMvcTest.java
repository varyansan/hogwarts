package com.example.shcool_hogwarts.WebMvcTest;

import com.example.shcool_hogwarts.controller.StudentController;
import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;
import com.example.shcool_hogwarts.repositories.AvatarRepository;
import com.example.shcool_hogwarts.repositories.FacultyRepository;
import com.example.shcool_hogwarts.repositories.StudentRepository;
import com.example.shcool_hogwarts.service.impl.AvatarServiceImpl;
import com.example.shcool_hogwarts.service.impl.FacultyServiceImpl;
import com.example.shcool_hogwarts.service.impl.StudentServiceImpl;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StudentControllerWebMvcTest {
    final String name = "Олег";
    final int age = 18;
    final long id = 1L;

    final String name2 = "Лейла";
    final int age2 = 16;
    final long id2 = 2L;

    final String name3 = "Борис";
    final int age3 = 13;
    final long id3 = 3L;

    final long facultyId = 1L;
    final String facultyName = "Диплом";
    final String facultyColor = "Black";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private StudentServiceImpl studentService;

    @SpyBean
    private AvatarServiceImpl avatarService;

    @SpyBean
    private FacultyServiceImpl facultyService;

    @InjectMocks
    private StudentController studentController;

    @Test
    @DisplayName("Тест на сохранение объекта Студент")
    public void saveStudentTest() throws Exception {

        JSONObject studentJson = new JSONObject();
        studentJson.put("name", name);
        studentJson.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student/add-student")
                        .content(studentJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    @DisplayName("Тест на поиск объекта Студент")
    public void findStudentTest() throws Exception {

        JSONObject studentJson = new JSONObject();
        studentJson.put("name", name);
        studentJson.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id + "/find-student")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    @DisplayName("Тест на обновление объекта Студент")
    public void editeStudentTest() throws Exception {

        final long idStudentOld = 1L;

        Student studentOld = new Student();
        studentOld.setId(idStudentOld);
        studentOld.setName(name2);
        studentOld.setAge(age2);

        JSONObject studentJsonOld = new JSONObject();
        studentJsonOld.put("name", name2);
        studentJsonOld.put("age", age2);

        Student student = new Student();
        student.setId(idStudentOld);
        student.setName(name);
        student.setAge(age);

        JSONObject studentJson = new JSONObject();
        studentJson.put("id", idStudentOld);
        studentJson.put("name", name);
        studentJson.put("age", age);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(studentOld));
        when(studentRepository.existsById(idStudentOld)).thenReturn(true);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + idStudentOld + "/edite")
                        .content(studentJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idStudentOld))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    @DisplayName("Тест на удаление объекта Студент")
    public void deleteStudentTest() throws Exception {

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        when(studentRepository.existsById(id)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + id + "/delete-student")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест на поиск студентов по возрасту")
    public void getStudentsByAgeTest() throws Exception {
        int minAge = 15;
        int maxAge = 20;

        Student student1 = new Student();
        student1.setId(id);
        student1.setName(name);
        student1.setAge(age);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        Student student3 = new Student();
        student3.setId(id3);
        student3.setName(name3);
        student3.setAge(age3);

        List<Student> students = Arrays.asList(student1, student2);

        when(studentService.findByAgeBetween(minAge, maxAge)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/by-age")
                        .param("minAge", String.valueOf(minAge))
                        .param("maxAge", String.valueOf(maxAge))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(students.size()))
                .andExpect(jsonPath("$[0].id").value(student1.getId()))
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[0].age").value(student1.getAge()))
                .andExpect(jsonPath("$[1].id").value(student2.getId()))
                .andExpect(jsonPath("$[1].name").value(student2.getName()))
                .andExpect(jsonPath("$[1].age").value(student2.getAge()));
    }

    @Test
    @DisplayName("Тест на получение факультета студента по ID")
    public void getFacultyByStudentIdTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(facultyColor);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id + "/faculty-by-student")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    @DisplayName("Тест на назначение факультета студенту")
    public void assignFacultyToStudentTest() throws Exception {
        Long studentId = 1L;
        Long facultyId = 2L;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName("Magic");
        faculty.setColor("Blue");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + studentId + "/faculty/" + facultyId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
