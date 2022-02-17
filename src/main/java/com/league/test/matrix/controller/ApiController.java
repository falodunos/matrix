package com.league.test.matrix.controller;

import com.league.test.matrix.dto.response.BaseResponse;
import com.league.test.matrix.dto.response.UploadFileResponse;
import com.league.test.matrix.service.CsvFileProcessorService;
import com.league.test.matrix.service.FileStorageService;
import com.league.test.matrix.service.util.AppConstants;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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

    /**
     * Print the matrix representation of the supplied CSV file
     * @param file
     * @return
     * @throws IOException
     * @throws CsvValidationException
     */
    @PostMapping("/echo")
    public String echo(@RequestParam("file") MultipartFile file) throws IOException, CsvValidationException {

        BaseResponse uploadFileResponse = fileStorageService.uploadFile(file);
        String matrix = "";
        if (uploadFileResponse.getStatus().equals("200")) {
            matrix = this.csvFileProcessorService.echo();
        }
        return matrix;
    }

    /**
     * Print the inverted matrix representation of the supplied CSV file
     * @param file
     * @return
     * @throws CsvValidationException
     * @throws IOException
     */
    @PostMapping("/invert")
    public String invert(@RequestParam("file") MultipartFile file) throws CsvValidationException, IOException {
        BaseResponse uploadFileResponse = fileStorageService.uploadFile(file);
        String matrix = "";
        if (uploadFileResponse.getStatus().equals("200")) {
            matrix = this.csvFileProcessorService.invert();
        }
        return matrix;
    }

    /**
     * Flatten and print the matrix representation of the supplied CSV file
     * @param file
     * @return
     * @throws CsvValidationException
     * @throws IOException
     */
    @PostMapping("/flatten")
    public String flatten(@RequestParam("file") MultipartFile file) throws CsvValidationException, IOException {
        BaseResponse uploadFileResponse = fileStorageService.uploadFile(file);
        String matrix = "";
        if (uploadFileResponse.getStatus().equals("200")) {
            matrix = this.csvFileProcessorService.flatten();
        }
        return matrix;
    }

    /**
     * Sum and print all elements of the matrix representation in the supplied CSV file
     * @param file
     * @return
     * @throws CsvValidationException
     * @throws IOException
     */
    @PostMapping("/sum")
    public String sum(@RequestParam("file") MultipartFile file) throws CsvValidationException, IOException {
        BaseResponse uploadFileResponse = fileStorageService.uploadFile(file);
        String sum = "";
        if (uploadFileResponse.getStatus().equals("200")) {
            sum = this.csvFileProcessorService.sum();
        }
        return String.valueOf(sum);
    }

    /**
     * Multiply and print all elements of the matrix representation in the supplied CSV file
     * @param file
     * @return
     * @throws CsvValidationException
     * @throws IOException
     */
    @PostMapping("/multiply")
    public String multiply(@RequestParam("file") MultipartFile file) throws CsvValidationException, IOException {
        BaseResponse uploadFileResponse = fileStorageService.uploadFile(file);
        String multiply = "";
        if (uploadFileResponse.getStatus().equals("200")) {
            multiply = this.csvFileProcessorService.multiply();
        }
        return String.valueOf(multiply);
    }
}
