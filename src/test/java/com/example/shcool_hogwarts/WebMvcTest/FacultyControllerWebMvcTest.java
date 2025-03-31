package com.example.shcool_hogwarts.WebMvcTest;

import com.example.shcool_hogwarts.controller.FacultyController;
import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;
import com.example.shcool_hogwarts.repositories.AvatarRepository;
import com.example.shcool_hogwarts.repositories.FacultyRepository;
import com.example.shcool_hogwarts.repositories.StudentRepository;
import com.example.shcool_hogwarts.service.impl.AvatarServiceImpl;
import com.example.shcool_hogwarts.service.impl.FacultyServiceImpl;
import com.example.shcool_hogwarts.service.impl.StudentServiceImpl;
import org.assertj.core.api.AssertionsForClassTypes;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class FacultyControllerWebMvcTest {
    final long id = 1L;
    final String name = "Диплом";
    final String color = "Black";

    final long id2 = 2L;
    final String name2 = "Аркейн";
    final String color2 = "Blue";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @SpyBean
    private FacultyServiceImpl facultyService;

    @SpyBean
    private AvatarServiceImpl avatarService;

    @SpyBean
    private StudentServiceImpl studentService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    @DisplayName("Тест на поиск объекта Факультет")
    void getFindFaculty() throws Exception {

        JSONObject facultyJson = new JSONObject();
        facultyJson.put("name", name);
        facultyJson.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/" + id + "/find-faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    @DisplayName("Тест на сохранение объекта Факультет")
    void createFaculty() throws Exception {

        JSONObject facultyJson = new JSONObject();
        facultyJson.put("name", name);
        facultyJson.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty/add-faculty")
                        .content(facultyJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    @DisplayName("Тест на обновление объекта Факультет")
    void editFaculty() throws Exception {

        final long idFacultyOld = 1L;

        Faculty facultyOld = new Faculty();
        facultyOld.setId(idFacultyOld);
        facultyOld.setName(name2);
        facultyOld.setColor(color2);

        JSONObject facultyJsonOld = new JSONObject();
        facultyJsonOld.put("name", name2);
        facultyJsonOld.put("color", color2);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        JSONObject facultyJson = new JSONObject();
        facultyJson.put("name", name);
        facultyJson.put("color", color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(facultyOld));
        when(facultyRepository.existsById(idFacultyOld)).thenReturn(true);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + idFacultyOld + "/update")
                        .content(facultyJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idFacultyOld))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    @DisplayName("Тест на удаление объекта Факультет")
    void deleteFaculty() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        when(facultyRepository.existsById(id)).thenReturn(true);
        doNothing().when(facultyRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id + "/delete")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(facultyRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Тест на поиск по цвету Студентов Факультета")
    void filterColor() throws Exception {
        String paramColor = "Black";

        final long id3 = 3L;
        final String color3 = "Black";
        final String name3 = "Python";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color2);

        Faculty faculty3 = new Faculty();
        faculty3.setId(id3);
        faculty3.setName(name3);
        faculty3.setColor(color3);

        List<Faculty> facultyList = Arrays.asList(faculty, faculty3);

        when(facultyRepository.findByColorContainingIgnoreCase(paramColor)).thenReturn(facultyList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color")
                        .param("color", paramColor)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(facultyList.size()))
                .andExpect(jsonPath("$[0].id").value(faculty.getId()))
                .andExpect(jsonPath("$[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$[0].color").value(faculty.getColor()))
                .andExpect(jsonPath("$[1].id").value(faculty3.getId()))
                .andExpect(jsonPath("$[1].name").value(faculty3.getName()))
                .andExpect(jsonPath("$[1].color").value(faculty3.getColor()));
    }

    @Test
    @DisplayName("Тест на получение студентов по ID факультета")
    public void getStudentsByFacultyTest() {

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Oleg");
        student1.setAge(18);
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Stas");
        student2.setAge(15);
        student2.setFaculty(faculty);

        List<Student> students = Arrays.asList(student1, student2);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        when(studentRepository.findAllByFaculty(faculty)).thenReturn(students);

        List<Student> result = facultyService.getStudentsByFaculty(id);

        assertThat(result).hasSize(2);
        AssertionsForClassTypes.assertThat(result.get(0).getId()).isEqualTo(student1.getId());
        AssertionsForClassTypes.assertThat(result.get(0).getName()).isEqualTo(student1.getName());
        AssertionsForClassTypes.assertThat(result.get(0).getAge()).isEqualTo(student1.getAge());
        AssertionsForClassTypes.assertThat(result.get(1).getId()).isEqualTo(student2.getId());
        AssertionsForClassTypes.assertThat(result.get(1).getName()).isEqualTo(student2.getName());
        AssertionsForClassTypes.assertThat(result.get(1).getAge()).isEqualTo(student2.getAge());
    }
}
