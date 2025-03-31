package com.example.shcool_hogwarts.controller;
import com.example.shcool_hogwarts.model.Avatar;
import com.example.shcool_hogwarts.service.AvatarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/avatar")
@Tag(name = "Контроллер аватаров", description = "Контроллеры для работы с аватарами студентов")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    /**
     * @param studentId получает студента по ID для поиска аватара
     * @return возвращает статус запроса
     * @throws IOException сообщение об ошибке: аватар = null
     */
    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загружает аватар для студента",
            description = "Загружает аватар для студента по ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Аватар успешно загружен"),
                    @ApiResponse(responseCode = "404", description = "Студент не найден")
            })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> uploadAvatar(@PathVariable Long studentId,
                                             @RequestParam MultipartFile avatar) throws IOException {
        if (avatar != null) {
            avatarService.uploadAvatar(studentId, avatar);
            return ResponseEntity.noContent().build();
        } else {
            throw new IllegalArgumentException("Аватар не может быть null");
        }
    }

    /**
     * @param id идентификатор студента, по которому будет поиск в БД
     * @return найденный файл аватара, либо ошибка not found
     */

    @GetMapping(value = "/{id}/avatar-from-db")
    @Operation(summary = "Получить аватар из базы данных",
            description = "Возвращает аватар по ID студента из базы данных",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аватар найден"),
                    @ApiResponse(responseCode = "404", description = "Аватар не найден")
            })
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);
        if (avatar != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(avatar.getMediaType()));
            headers.setContentLength(avatar.getData().length);
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * @param id       идентификатор студента, по которому производится поиск аватара
     * @param response Обработка исключения
     * @throws IOException 404
     */
    @GetMapping(value = "/{id}/avatar-from-file")
    @Operation(summary = "Получить аватар из файла",
            description = "Возвращает аватар по ID из файла",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аватар найден"),
                    @ApiResponse(responseCode = "404", description = "Аватар не найден")
            })
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatar(id);
        if (avatar != null) {
            Path path = Path.of(avatar.getFilePath());
            try (InputStream is = Files.newInputStream(path);
                 OutputStream os = response.getOutputStream();) {
                response.setStatus(200);
                response.setContentType(avatar.getMediaType());
                response.setContentLength((int) avatar.getFileSize());
                is.transferTo(os);
            } catch (IOException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Аватар не найден");
            }
        }
    }
    /**
     * Получение списка аватаров с пагинацией
     *
     * @param pageNumber Номер страницы
     * @param pageSize   Размер страницы
     * @return ResponseEntity с Page&lt;Avatar&gt;- содержит список аватарок и информацию о пагинации.
     * Возвращает 200 OK с данными или 500 в случае ошибки.
     */
    @GetMapping("/pageable")
    @Operation(summary = "Получение списка аватаров с пагинацией", description = "Возвращает список аватаров с учетом пагинации.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка аватаров",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Page<Avatar>> getAllAvatars(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(avatarService.getAllAvatars(pageable));
    }
}

