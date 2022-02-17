package com.league.test.matrix.controller;

import com.league.test.matrix.dto.response.BaseResponse;
import com.league.test.matrix.dto.response.UploadFileResponse;
import com.league.test.matrix.service.CsvFileProcessorService;
import com.league.test.matrix.service.FileStorageService;
import com.league.test.matrix.service.util.AppConstants;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ApiController.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FileStorageService fileStorageService;

    @MockBean
    CsvFileProcessorService csvFileProcessorService;

    MockMultipartFile file = new MockMultipartFile(
            "file", "matrix.csv",
            MediaType.TEXT_PLAIN_VALUE, "1,2,3\n4,5,6\n7,8,9".getBytes());

    UploadFileResponse uploadFileResponse = new UploadFileResponse("matrix.csv", "http://localhost:8090/downloadFile/matrix.csv", "text/csv", 17);
    BaseResponse response = new BaseResponse(HttpStatus.OK.value() + "", AppConstants.ApiResponseMessage.SUCCESSFUL, uploadFileResponse);


    @Test
    public void echoTest() throws Exception {
        when(fileStorageService.uploadFile(file)).thenReturn(response);
        when(csvFileProcessorService.echo()).thenReturn("1,2,3\n4,5,6\n7,8,9");

        RequestBuilder request = MockMvcRequestBuilders
                .multipart("/api/matrix/v1/echo")
                .file(file)
                .accept(MediaType.MULTIPART_FORM_DATA);

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().string("1,2,3\n4,5,6\n7,8,9"))
                .andReturn();
    }

    @Test
    public void invertTest() throws Exception {
        when(fileStorageService.uploadFile(file)).thenReturn(response);
        when(csvFileProcessorService.invert()).thenReturn("1,4,7\n2,5,8\n3,6,9");

        RequestBuilder request = MockMvcRequestBuilders
                .multipart("/api/matrix/v1/invert")
                .file(file)
                .accept(MediaType.MULTIPART_FORM_DATA);

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().string("1,4,7\n2,5,8\n3,6,9"))
                .andReturn();
    }

    @Test
    public void flattenTest() throws Exception {
        when(fileStorageService.uploadFile(file)).thenReturn(response);
        when(csvFileProcessorService.flatten()).thenReturn("1,2,3,4,5,6,7,8,9");

        RequestBuilder request = MockMvcRequestBuilders
                .multipart("/api/matrix/v1/flatten")
                .file(file)
                .accept(MediaType.MULTIPART_FORM_DATA);

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().string("1,2,3,4,5,6,7,8,9"))
                .andReturn();
    }

    @Test
    public void sumTest() throws Exception {
        when(fileStorageService.uploadFile(file)).thenReturn(response);
        when(csvFileProcessorService.sum()).thenReturn("45");

        RequestBuilder request = MockMvcRequestBuilders
                .multipart("/api/matrix/v1/sum")
                .file(file)
                .accept(MediaType.MULTIPART_FORM_DATA);

        MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().string("45"))
                .andReturn();

//        assert Integer.parseInt(result.getResponse().getContentAsString()) == 45;
    }

    @Test
    public void multiplyTest() throws Exception {
        when(fileStorageService.uploadFile(file)).thenReturn(response);
        when(csvFileProcessorService.multiply()).thenReturn("362880");

        RequestBuilder request = MockMvcRequestBuilders
                .multipart("/api/matrix/v1/multiply")
                .file(file)
                .accept(MediaType.MULTIPART_FORM_DATA);

        MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().string("362880"))
                .andReturn();

//        assert Integer.parseInt(result.getResponse().getContentAsString()) == 362880;
    }
}
