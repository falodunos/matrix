package com.league.test.matrix.controller;

import com.league.test.matrix.dto.response.BaseResponse;
import com.league.test.matrix.dto.response.UploadFileResponse;
import com.league.test.matrix.service.CsvFileProcessorService;
import com.league.test.matrix.service.FileStorageService;
import com.league.test.matrix.service.util.AppConstants;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;

@SuppressWarnings({"unchecked", "rawtypes"})
@RestController
@RequestMapping(value = AppConstants.APP_CONTEXT)
@Slf4j
public class ApiController {
    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    CsvFileProcessorService csvFileProcessorService;

    @PostMapping("/echo")
    public String echo(@RequestParam("file") MultipartFile file) throws IOException, CsvValidationException {

        BaseResponse uploadFileResponse = uploadFile(file);
        if (uploadFileResponse.getStatus().equals("200")) {
            String matrix = this.csvFileProcessorService.echo();
            return matrix;
        }
        return "unknown yet";
    }

    @PostMapping("/invert")
    public String invert(@RequestParam("file") MultipartFile file) {
        return null;
    }

    @PostMapping("/flatten")
    public String flatten(@RequestParam("file") MultipartFile file) {
        return null;
    }

    @PostMapping("/sum")
    public String sum(@RequestParam("file") MultipartFile file) {
        return null;
    }

    @PostMapping("/multiply")
    public String multiply(@RequestParam("file") MultipartFile file) {
        return null;
    }

    private BaseResponse uploadFile(MultipartFile file) {
        UploadFileResponse uploadFileResponse = null;
        BaseResponse response;

        if (fileStorageService.hasCSVFormat(file)) {
            try {
                String fileName = fileStorageService.storeFile(file);

                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/downloadFile/")
                        .path(fileName)
                        .toUriString();
                uploadFileResponse = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
            } catch (Exception ex) {
                ex.printStackTrace();
                return new BaseResponse(HttpStatus.EXPECTATION_FAILED.value() + "", ex.getMessage());
            }
        }

        response = uploadFileResponse != null ?
                new BaseResponse(HttpStatus.OK.value() + "", "Successful", uploadFileResponse) :
                new BaseResponse(HttpStatus.EXPECTATION_FAILED.value() + "", "Failed, please upload a valid CSV file");

        log.info("CSV File Upload Response : " + response.toString());
        return response;
    }
}
