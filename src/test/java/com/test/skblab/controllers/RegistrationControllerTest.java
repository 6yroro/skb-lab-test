package com.test.skblab.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.skblab.exceptions.UserExistsException;
import com.test.skblab.models.UserRequestData;
import com.test.skblab.services.RegistrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Alexander Zubkov
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class RegistrationControllerTest {

    @MockBean
    private RegistrationService registrationService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String rootPath;
    private final UserRequestData emptyData, notValidEmailData, validData;

    public RegistrationControllerTest() {
        String test = "test";
        rootPath = "/";
        emptyData = new UserRequestData();
        notValidEmailData = new UserRequestData();
        notValidEmailData.setLogin(test);
        notValidEmailData.setPassword(test);
        notValidEmailData.setEmail(test);
        notValidEmailData.setFirstName(test);
        notValidEmailData.setSecondName(test);
        notValidEmailData.setMiddleName(test);
        validData = new UserRequestData();
        validData.setLogin(test);
        validData.setPassword(test);
        validData.setEmail("test@test");
        validData.setFirstName(test);
        validData.setSecondName(test);
        validData.setMiddleName(test);
    }

    @Test
    public void getRoot() throws Exception {
        mvc.perform(get(rootPath))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException().getClass())
                        .isEqualTo(HttpRequestMethodNotSupportedException.class));
    }

    @Test
    public void postRoot() throws Exception {
        mvc.perform(post(rootPath))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException().getClass())
                        .isEqualTo(HttpMessageNotReadableException.class));
    }

    @Test
    public void postRootEmptyData() throws Exception {
        mvc.perform(post(rootPath)
                    .content(objectMapper.writeValueAsString(emptyData))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException().getClass())
                        .isEqualTo(MethodArgumentNotValidException.class));
    }

    @Test
    public void postRootNotValidEmailData() throws Exception {
        mvc.perform(post(rootPath)
                    .content(objectMapper.writeValueAsString(notValidEmailData))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException().getClass())
                        .isEqualTo(MethodArgumentNotValidException.class));
    }

    @Test
    public void postRootValidData() throws Exception {
        mvc.perform(post(rootPath)
                    .content(objectMapper.writeValueAsString(validData))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void postRootUserExists() throws Exception {
        doThrow(new UserExistsException("")).when(registrationService).userRegistration(any());
        mvc.perform(post(rootPath)
                    .content(objectMapper.writeValueAsString(validData))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> assertThat(result.getResolvedException().getClass())
                        .isEqualTo(UserExistsException.class));
    }

}