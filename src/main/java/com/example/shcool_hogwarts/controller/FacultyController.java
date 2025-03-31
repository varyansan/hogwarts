package com.example.shcool_hogwarts.controller;

import com.example.shcool_hogwarts.model.Faculty;
import com.example.shcool_hogwarts.model.Student;
import com.example.shcool_hogwarts.service.FacultyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
@Tag(name = "Контроллер факультетов", description = "Контроллеры для добавления факультетов")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    /**
     * @param id идентификатор факультета
     * @return возвращает id факультета, если найден
     */
    @GetMapping("{id}/find-faculty")
    @Operation(summary = "Ищет факультет",
            description = "Ищет факультет по id",
            responses = {@ApiResponse(responseCode = "404", description = "Факультет не найден"),
                    @ApiResponse(responseCode = "200", description = "Факультет найден")
            })
    public Faculty getFindFaculty(@PathVariable long id) {
        return facultyService.findFaculty(id);
    }

    /**
     * @param faculty факультет для добавления
     * @return возвращает созданный факультет
     */

    @PostMapping("/add-faculty")
    @Operation(summary = "Создает факультет",
            description = "Создает новый факультет",
            responses = @ApiResponse(responseCode = "200", description = "Факультет создан"))
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    /**
     * @param id      идентификатор факультета
     * @param faculty факультет для обновления
     * @return новый факультет
     */

    @PutMapping("{id}/update")
    @Operation(summary = "Обновляет факультет",
            description = "Обновляет факультет и устанавливает id",
            responses = {@ApiResponse(responseCode = "404", description = "Факультет не найден"),
                    @ApiResponse(responseCode = "200", description = "Факультет найден")
            })
    public Faculty editFaculty(@PathVariable("id") long id,
                               @RequestBody Faculty faculty) {
        faculty.setId(id);
        return facultyService.editFaculty(faculty);
    }

    /**
     * @param id факультета для удаления
     * @return возвращает удаленный факультет (тип Faculty) или null, если факультет с заданным id не был найден
     */

    @DeleteMapping("{id}/delete")
    @Operation(summary = "Удаляет факультет",
            description = "Удаляет факультет по id",
            responses = {@ApiResponse(responseCode = "404", description = "Факультет для удаления не найден"),
                    @ApiResponse(responseCode = "204", description = "Факультет удален")
            })
    public ResponseEntity deleteFaculty(@PathVariable("id") long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * @param color цвет, по которому будет поиск для фильтрации. String
     * @return Метод filterByColor возвращает список Faculty (тип Collection<Faculty>), которые имеют заданный цвет (color)
     */

    @GetMapping("/color")
    @Operation(summary = "Ищет по цвету",
            description = "Ищет факультет по цвету, возвращает коллекцию",
            responses = {@ApiResponse(responseCode = "404", description = "Факультет не найден"),
                    @ApiResponse(responseCode = "200", description = "Факультет найден")
            })
    public Collection<Faculty> filterColor(@RequestParam("color") String color) {
        return facultyService.findByColor(color);
    }

    /**
     * @param id принимает идентификатор факультета
     * @return возвращает список студентов факультета
     */

    @GetMapping("/{id}/students-faculty")
    @Operation(summary = "Ищет по факультету",
            description = "Ищет студентов по идентификатору факультета, возвращает коллекцию выбранного факультета",
            responses = {@ApiResponse(responseCode = "404", description = "Факультет не найден"),
                    @ApiResponse(responseCode = "200", description = "Факультет найден")
            })
    public Collection<Student> getStudentsByFaculty(@PathVariable Long id) {
        return facultyService.getStudentsByFaculty(id);
    }
}
