package com.example.shcool_hogwarts.controller;

import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;
import com.example.shcool_hogwarts.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/student")
@Tag(name = "Контроллер студентов", description = "Контроллеры для управления методами класса студент")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param id идентификатор студента
     * @return возвращает id студента, если найден, иначе null+exception
     */

    @GetMapping("{id}/find-student")
    @Operation(summary = "Ищет студента",
            description = "Ищет студента по его id",
            responses = {@ApiResponse(responseCode = "404", description = "Студент не найден"),
                    @ApiResponse(responseCode = "200", description = "Студент найден")})
    public Student getFindStudent(@PathVariable long id) {
        return studentService.findStudent(id);
    }

    /**
     * @param student набор параметров студента для добавления
     * @return возвращает counter - установленный id
     */
    @PostMapping("/add-student")
    @Operation(summary = "Добавляет студента",
            description = "Добавляет студента и устанавливает id",
            responses = @ApiResponse(responseCode = "200", description = "Студент добавлен"))
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    /**
     * @param id      идентификатор студента
     * @param student студент для обновления
     * @return возвращает нового студента
     */
    @PutMapping("{id}/edite")
    @Operation(summary = "Обновляет студента",
            description = "Обновляет студента и устанавливает id",
            responses = {@ApiResponse(responseCode = "404", description = "Студент не найден"),
                    @ApiResponse(responseCode = "200", description = "Студент найден")
            })
    public Student editStudent(@PathVariable("id") long id,
                               @RequestBody Student student) {
        return studentService.editStudent(student);
    }

    /**
     * @param id идентификатор студента
     * @return возвращает удаленного студента или null, если студент с заданным id не был найден
     */
    @DeleteMapping("{id}/delete-student")
    @Operation(summary = "Удаляет студента",
            responses = {@ApiResponse(responseCode = "404", description = "Студент не найден"),
                    @ApiResponse(responseCode = "200", description = "Студент удален")
            })
    public ResponseEntity deleteStudent(@PathVariable("id") long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    /**
     * @param minAge параметр мин возраста студента
     * @param maxAge параметр макс возраста студента
     * @return возвращает коллекцию по указанному возрасту
     */

    @GetMapping("/by-age")
    @Operation(summary = "Поиск студентов по возрасту",
            description = "Возвращает список студентов, чей возраст находится в заданном диапазоне.",
            parameters = {
                    @Parameter(name = "minAge", description = "Минимальный возраст", required = true,
                            schema = @Schema(type = "integer")),
                    @Parameter(name = "maxAge", description = "Максимальный возраст", required = true,
                            schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список студентов, соответствующих критериям"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
            })
    public Collection<Student> getStudentsByAge(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.findByAgeBetween(minAge, maxAge);
    }

    /**
     * @param id идентификатор студента
     * @return факультет этого студента
     */
    @GetMapping("/{id}/faculty-by-student")
    @Operation(summary = "Получить факультет студента",
            description = "Возвращает факультет студента по его ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Факультет найден"),
                    @ApiResponse(responseCode = "404", description = "Студент не найден")
            })
    public Faculty getFacultyByStudentId(@PathVariable Long id) {
        return studentService.getFacultyByStudentId(id);
    }

    /**
     * @param studentId идентификатор студента
     * @param facultyId идентификатор факультета
     * @return статус 200 ок
     */

    @PutMapping("/{studentId}/faculty/{facultyId}")
    @Operation(summary = "Назначить факультет студенту",
            description = "Присваивает указанный факультет студенту по их ID.",
            parameters = {
                    @Parameter(name = "studentId", description = "ID студента", required = true, schema = @Schema(type = "integer")),
                    @Parameter(name = "facultyId", description = "ID факультета", required = true, schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Факультет успешно назначен"),
                    @ApiResponse(responseCode = "404", description = "Студент или факультет не найден")
            })
    public ResponseEntity<Void> assignFaculty(@PathVariable Long studentId, @PathVariable Long facultyId) {
        studentService.assignFacultyToStudent(studentId, facultyId);
        return ResponseEntity.ok().build();
    }
}
