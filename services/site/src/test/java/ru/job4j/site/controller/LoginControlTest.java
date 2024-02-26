package ru.job4j.site.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.site.SiteSrv;
import ru.job4j.site.domain.Breadcrumb;
import ru.job4j.site.dto.CredentialDTO;
import ru.job4j.site.service.AuthService;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CheckDev пробное собеседование
 * LoginControlTest тесты на контроллер LoginController
 *
 * @author Dmitry Stepanov
 * @version 24.09.2023 22:44
 */
@SpringBootTest(classes = SiteSrv.class)
@AutoConfigureMockMvc
class LoginControlTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;

    private LoginControl loginControl;

    @BeforeEach
    void initTest() {
        loginControl = new LoginControl(authService);
    }

    @Test
    void whenGetLoginPageThenReturnLogin() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void whenPostSignInThenRedirectStartPage() throws Exception {
        var catDTO = new CredentialDTO();
        catDTO.setEmail("email");
        catDTO.setPassword("pass");
        var isLogin = "12345678";
        when(authService.token(
                Map.of("username", catDTO.getEmail(),
                        "password", catDTO.getPassword())))
                .thenReturn(isLogin);
        this.mockMvc.perform(post("/signIn")
                        .flashAttr("redirectUri", new String())
                        .flashAttr("topicId", new String())
                        .param("email", "email")
                        .param("password", "pass"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void whenRedirectFromCreateInterviewAndBack() throws Exception {
        var catDTO = new CredentialDTO();
        catDTO.setEmail("email");
        catDTO.setPassword("pass");
        var isLogin = "12345678";
        when(authService.token(
                Map.of("username", catDTO.getEmail(),
                        "password", catDTO.getPassword())))
                .thenReturn(isLogin);
        this.mockMvc.perform(post("/signIn")
                        .flashAttr("redirectUri", "/interview/createForm")
                        .flashAttr("topicId", "test")
                        .param("email", "email")
                        .param("password", "pass"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/interview/createForm"));
    }

    @Test
    void whenPostSignInThenRedirectLoginErrorTrue() throws Exception {
        var catDTO = new CredentialDTO();
        catDTO.setEmail("email");
        catDTO.setPassword("pass");
        when(authService.token(
                Map.of("username", catDTO.getEmail(),
                        "password", catDTO.getPassword())))
                .thenReturn("");
        this.mockMvc.perform(post("/signIn")
                        .param("email", "email")
                        .param("password", "pass"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }


    @Test
    void whenLogoutRedirectStartPage() throws Exception {
        this.mockMvc.perform(get("/logout"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void whenLogoutPageThenSessionInvalidate() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("token", 123456);
        var actualPage = loginControl.logout(request);
        String token = (String) request.getSession().getAttribute("token");
        assertThat(actualPage).isEqualTo("redirect:/");
        assertThat(token).isNull();
    }

    @Test
    void whenGetRegistrationThenReturnRegistrationPage() throws Exception {
        var breadcrumbs = List.of(new Breadcrumb("Главная", "/"), new Breadcrumb("Регистрация", "/registration"));
        this.mockMvc.perform(get("/registration"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("breadcrumbs", breadcrumbs))
                .andExpect(view().name("registration"));
    }
}