package com.league.test.matrix.service;


import com.league.test.matrix.config.AppConfig;
import com.league.test.matrix.dto.response.BaseResponse;
import com.league.test.matrix.dto.response.UploadFileResponse;
import com.league.test.matrix.exceptions.FileStorageException;
import com.league.test.matrix.service.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
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

    /**
     * Upload file to local directory
     * @param file
     * @return String
     */
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            this.renameFile(fileName); // always rename uploaded file to matrix.csv to ensure easy accessibility

            return AppConstants.TARGET_FILE_NAME;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Rename uploaded file name to matrix.csv
     * @param fileName
     * @throws IOException
     * @return void
     */
    private void renameFile(String fileName) throws IOException {
        File fileToMove = new File(AppConstants.FILE_UPLOAD_PATH + fileName);
        boolean isMoved = fileToMove.renameTo(new File(AppConstants.TARGET_FILE_PATH));
        if (!isMoved) {
            throw new FileSystemException(AppConstants.TARGET_FILE_PATH);
        }
    }

    /**
     * Check if file to be uploaded is a .csv file
     * @param file
     * @return boolean
     */
    public boolean hasCSVFormat(MultipartFile file) {
        if (!AppConstants.FILE_TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    /**
     * Perform file upload to a local folder or directory
     * @param file
     * @return
     */
    public BaseResponse uploadFile(MultipartFile file) {
        UploadFileResponse uploadFileResponse = null;
        BaseResponse response;

        if (hasCSVFormat(file)) {
            try {
                String fileName = storeFile(file); // upload file and return uploaded file name

                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/downloadFile/")
                        .path(fileName)
                        .toUriString();
                // aggregate response for the upload file
                uploadFileResponse = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
            } catch (Exception ex) {
                ex.printStackTrace();
                return new BaseResponse(HttpStatus.EXPECTATION_FAILED.value() + "", ex.getMessage());
            }
        }

        response = uploadFileResponse != null ?
                new BaseResponse(HttpStatus.OK.value() + "", AppConstants.ApiResponseMessage.SUCCESSFUL, uploadFileResponse) :
                new BaseResponse(HttpStatus.EXPECTATION_FAILED.value() + "", "Failed, please upload a valid CSV file");

        log.info("CSV File Upload Response : " + response.toString());
        return response;
    }
}
