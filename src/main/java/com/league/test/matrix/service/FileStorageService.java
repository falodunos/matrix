package com.league.test.matrix.service;


import com.league.test.matrix.config.AppConfig;
import com.league.test.matrix.exceptions.FileStorageException;
import com.league.test.matrix.exceptions.MyFileNotFoundException;
import com.league.test.matrix.service.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
@Slf4j
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(AppConfig appConfig) {
        this.fileStorageLocation = Paths.get(AppConstants.FILE_UPLOAD_PATH)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            this.renameFile(fileName);

            return AppConstants.TARGET_FILE_NAME;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private void renameFile(String fileName) throws IOException {
        File fileToMove = new File(AppConstants.FILE_UPLOAD_PATH + fileName);
        boolean isMoved = fileToMove.renameTo(new File(AppConstants.TARGET_FILE_PATH));
        if (!isMoved) {
            throw new FileSystemException(AppConstants.TARGET_FILE_PATH);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public boolean hasCSVFormat(MultipartFile file) {
        if (!AppConstants.FILE_TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }
}
