package com.example.shcool_hogwarts.controller;

import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;
import com.example.shcool_hogwarts.model.StudentProjection;
import com.example.shcool_hogwarts.repositories.StudentRepository;
import com.example.shcool_hogwarts.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RestController
@RequestMapping("/student")
@Tag(name = "Контроллер студентов", description = "Контроллеры для управления методами класса студент")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    private static String apply(Student student) {
        return student.getName().toUpperCase();
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
    /**
     * @return статус 200, количество студентов
     */
    @GetMapping("/count")
    @Operation(summary = "SQL запрос - получение количества студентов школы")
    public ResponseEntity<Long> countStudents() {
        return ResponseEntity.ok(studentService.countAllStudents());
    }

    /**
     * @return статус 200, средний возраст студентов
     */
    @GetMapping("/average-age")
    @Operation(summary = "SQL запрос - получение среднего возраста студентов")
    public ResponseEntity<Double> AverageAge() {
        return ResponseEntity.ok(studentService.getAverageAge());
    }

    /**
     * @return статус 200, последние 5 студентов по идентификатору
     */
    @GetMapping("/last-five")
    @Operation(summary = "SQL запрос - получение последних 5 студентов по идентификатору")
    public ResponseEntity<Page<StudentProjection>> lastFiveStudents() {
        return ResponseEntity.ok(studentService.findLastFiveStudents());
    }

    /**
     * @return список студентов в формате JSON, где имя начинается на "а" по алфавиту
     */
    @GetMapping("/namesStartingWithA") //toDo test
    @Operation(summary = "список студентов в формате JSON, где имя начинается на А по алфавиту")
    public List<Student> getStudentNamesStartingWithA() {
        return studentService.findAllStudent()
                .stream()
                .filter(student -> student.getName().toLowerCase().startsWith("а"))
                .peek(student -> student.setName(student.getName().toUpperCase()))
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }

    /**
     * @return средний возраст всех студентов
     */
    @GetMapping("/averageAge") //toDo test
    @Operation(summary = "средний возраст всех студентов")
    public double getAverageStudentAge() {
        return studentService.findAllStudent()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }

    @GetMapping("/sum")
    public long calculateSum() {
        return LongStream.rangeClosed(1, 1_000_000)
                .parallel()
                .sum();
    }

    @GetMapping("/print-parallel")
    public void printParallel() {
        studentService.printParallel();
    }

    @GetMapping("/print-synchronized")
    public void printSynchronized() {
        studentService.printSynchronized();
    }
}
