package ru.checkdev.mock.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.checkdev.mock.MockSrv;
import ru.checkdev.mock.domain.Filter;
import ru.checkdev.mock.service.FilterService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MockSrv.class)
@AutoConfigureMockMvc
public class FilterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilterService filterService;

    @Test
    public void whenFilterSaved() throws Exception {
        var filter = new Filter(1, 1, 1);
        when(filterService.save(filter)).thenReturn(Optional.of(filter));
        String json = new GsonBuilder().serializeNulls().create().toJson(filter);
        mockMvc.perform(post("/filter/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(filter)))
                .andDo(print()).andExpectAll(status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(json));
    }

    @Test
    public void whenFilterFindByUserId() throws Exception {
        var filter = new Filter(1, 1, 1);
        when(filterService.findByUserId(1)).thenReturn(Optional.of(filter));
        String json = new GsonBuilder().serializeNulls().create().toJson(filter);
        mockMvc.perform(get("/filter/1"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(json));
    }

    @Test
    public void whenFilterNotFindByUserId() throws Exception {
        var filter = new Filter();
        when(filterService.findByUserId(1)).thenReturn(Optional.empty());
        String json = new GsonBuilder().serializeNulls().create().toJson(filter);
        mockMvc.perform(get("/filter/1"))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(json));
    }

    @Test
    public void whenFilterDeleted() throws Exception {
        var filter = new Filter(1, 1, 1);
        when(filterService.deleteByUserId(1)).thenReturn(1);
        mockMvc.perform(delete("/filter/delete/1"))
                .andDo(print())
                .andExpectAll(status().isOk(),
                        content().string("true"));
    }

    @Test
    public void whenFilterCanNotBeDeleted() throws Exception {
        when(filterService.deleteByUserId(1)).thenReturn(0);
        mockMvc.perform(delete("/filter/delete/1"))
                .andDo(print())
                .andExpectAll(status().isNotFound(),
                        content().string("false"));
    }
}
