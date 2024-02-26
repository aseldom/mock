package ru.job4j.site.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.site.dto.PersonDTO;
import ru.job4j.site.service.ImageCompressorService;
import ru.job4j.site.service.PersonService;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CheckDev пробное собеседование
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 25.09.2023
 */
@SpringBootTest(classes = PersonController.class)
@AutoConfigureMockMvc
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PersonService personService;
    @MockBean
    private ImageCompressorService compressorService;
    @Value("${server.site.maxSizeLoadFile}")
    private String maxSizeFile;

    @Test
    void whenGetViewPersonThenReturnPersonViewPage() throws Exception {
        var token = "123";
        var person = new PersonDTO();
        person.setId(1);
        person.setUsername("username");
        person.setEmail("email");
        when(personService.getPerson(token)).thenReturn(person);
        this.mockMvc.perform(get("/persons/")
                        .sessionAttr("token", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("personDto", person))
                .andExpect(view().name("/persons/personView"));
    }

    @Test
    void whenGetViewPersonNullThenRedirectStartPage() throws Exception {
        this.mockMvc.perform(get("/persons/"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void getEditPersonThenReturnPersonViewPage() throws Exception {
        var token = "123";
        var person = new PersonDTO();
        person.setId(1);
        person.setUsername("username");
        person.setEmail("email");
        when(personService.getPerson(token)).thenReturn(person);
        this.mockMvc.perform(get("/persons/edit")
                        .sessionAttr("token", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("personDto", person))
                .andExpect(view().name("/persons/personEdit"));
    }

    @Test
    void getEditPersonNullThenRedirectStartPage() throws Exception {
        this.mockMvc.perform(get("/persons/edit"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void whenPostUpdatePersonThenRedirect() throws Exception {
        var token = "123";
        var person = new PersonDTO();
        person.setId(1);
        person.setUsername("username");
        person.setEmail("email");
        var fileSize = new byte[(Integer.parseInt(maxSizeFile) * 1024) / 10];
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                fileSize
        );
        when(compressorService.compressImage(file)).thenReturn(file);
        when(personService.getPerson(token)).thenReturn(person);
        this.mockMvc.perform(multipart("/persons/edit")
                        .file(file)
                        .accept(MediaType.MULTIPART_FORM_DATA)
                        .requestAttr("personDTO", person)
                        .sessionAttr("token", token))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/persons/"));
    }

    @Test
    void whenPostUpdatePersonThenRedirectErrorMessage() throws Exception {
        var token = "123";
        var person = new PersonDTO();
        person.setId(1);
        person.setUsername("username");
        person.setEmail("email");
        var fileSize = new byte[Integer.parseInt(maxSizeFile) * 1024 * 10];
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                fileSize
        );
        when(compressorService.compressImage(file)).thenThrow(IOException.class);
        when(personService.getPerson(token)).thenReturn(person);
        this.mockMvc.perform(multipart("/persons/edit")
                        .file(file)
                        .accept(MediaType.MULTIPART_FORM_DATA)
                        .requestAttr("personDTO", person)
                        .sessionAttr("token", token))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/persons/edit?error=true"));
    }

    @Test
    void whenPostUpdatePersonContentTypeNotJPEGThenRedirectErrorMessage() throws Exception {
        var token = "123";
        var person = new PersonDTO();
        person.setId(1);
        person.setUsername("username");
        person.setEmail("email");
        var fileSize = new byte[Integer.parseInt(maxSizeFile) * 1024 * 10];
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.jpeg",
                MediaType.IMAGE_PNG_VALUE,
                fileSize
        );
        when(compressorService.compressImage(file)).thenThrow(IOException.class);
        when(personService.getPerson(token)).thenReturn(person);
        this.mockMvc.perform(multipart("/persons/edit")
                        .file(file)
                        .accept(MediaType.MULTIPART_FORM_DATA)
                        .requestAttr("personDTO", person)
                        .sessionAttr("token", token))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/persons/edit?error=true"));
    }
}