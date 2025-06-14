package com.makarova.controllers;

import com.makarova.controllers.rest.AdvertRestController;
import com.makarova.dto.ApartmentDto;
import com.makarova.service.ApartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdvertRestController.class)
public class AdvertRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApartmentService apartmentService;

    @Test
    @WithMockUser(username = "test@example.com")
    public void testCreateAdvertSuccess() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        doNothing().when(apartmentService).saveAdvert(anyString(), any(ApartmentDto.class), any());

        mockMvc.perform(multipart("/api/adverts/add")
                .file(image)
                .param("title", "Test Apartment")
                .param("description", "Test Description")
                .param("price", "1000")
                .param("dealType", "RENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Объявление успешно добавлено"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testCreateAdvertWithoutImages() throws Exception {
        mockMvc.perform(multipart("/api/adverts/add")
                .param("title", "Test Apartment")
                .param("description", "Test Description")
                .param("price", "1000")
                .param("dealType", "RENT"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.mainPhotoUrl").value("Добавьте хотя бы одну фотографию"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testCreateAdvertWithInvalidData() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        doThrow(new RuntimeException("Invalid data"))
                .when(apartmentService).saveAdvert(anyString(), any(ApartmentDto.class), any());

        mockMvc.perform(multipart("/api/adverts/add")
                .file(image)
                .param("title", "")
                .param("description", "")
                .param("price", "-1000")
                .param("dealType", "INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Произошла ошибка при добавлении объявления: Invalid data"));
    }

    @Test
    public void testCreateAdvertUnauthorized() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/api/adverts/add")
                .file(image)
                .param("title", "Test Apartment")
                .param("description", "Test Description")
                .param("price", "1000")
                .param("dealType", "RENT"))
                .andExpect(status().isUnauthorized());
    }
} 