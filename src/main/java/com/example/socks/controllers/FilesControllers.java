package com.example.socks.controllers;
import com.example.socks.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


@RestController
@RequestMapping("/files")
@Tag(name = "Файлы", description = "Выгрузка файлов")
public class FilesControllers {
    private final FileService fileService;

    public FilesControllers(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value = "/export")
    @Operation(summary = "Экспорт файла в формате .json")
    public ResponseEntity<InputStreamResource> downloadFile() throws FileNotFoundException {
        File file = fileService.getFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"socks.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
