package com.example.socks.services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileService {

    @Value("${path.to.file.folder}")
    private String filePath;

    @Value("${path.of.file.name}")
    private String fileName;


    public File getFile() {
        return new File(filePath + "/" + fileName);
    }

    public void saveFile(String json) {
        cleanFile();
        try {
            Files.writeString(Path.of(filePath, fileName), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFromFile() {
        try {
            return Files.readString(Path.of(filePath, fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void cleanFile() {
        try {
            Files.deleteIfExists(Path.of(filePath, fileName));
            Files.createFile(Path.of(filePath, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

