package ru.job4j.site.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.site.dto.PersonDTO;
import ru.job4j.site.service.ImageCompress;
import ru.job4j.site.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Objects;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

/**
 * CheckDev пробное собеседование
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 18.09.2023
 */
@Controller
@RequestMapping("/persons")
@Slf4j
public class PersonController {
    private final String maxSizeFile;
    private final String contentTypeFile;
    private final PersonService personService;
    private final ImageCompress imageCompress;

    public PersonController(@Value("${server.site.maxSizeLoadFile}") String maxSizeFile,
                            @Value("${server.site.contentTypeFile}") String contentTypeFile,
                            PersonService personService, ImageCompress imageCompress) {
        this.maxSizeFile = maxSizeFile;
        this.contentTypeFile = contentTypeFile;
        this.personService = personService;
        this.imageCompress = imageCompress;
    }

    /**
     * Метод GET отображения страницы для просмотра данных пользователя.
     *
     * @param request HttpServletRequest
     * @param model   Model
     * @return String
     */
    @GetMapping("/")
    public String getViewPerson(HttpServletRequest request, Model model) {
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/",
                "Профиль", "/persons/"
        );
        var personDTO = getPersonDTO(request);
        if (personDTO == null) {
            return "redirect:/";
        }
        model.addAttribute("personDto", personDTO);
        model.addAttribute("photoId", getPhotoIdByPersonDTO(personDTO));
        return "/persons/personView";
    }

    /**
     * Метод GET отображения страницы для редактирования пользователя.
     *
     * @param request HttpServletRequest
     * @param model   Model
     * @return String
     */
    @GetMapping("/edit")
    public String getEditPerson(HttpServletRequest request,
                                Model model,
                                @RequestParam(value = "error", required = false) String error) {
        var personDTO = getPersonDTO(request);
        if (personDTO == null) {
            return "redirect:/";
        }
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/",
                "Профиль", "/persons/",
                "Редактирование", "/persons/edit"
        );
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Файл изображения не соответствует ограничениям";
        }
        model.addAttribute("personDto", personDTO);
        model.addAttribute("photoId", getPhotoIdByPersonDTO(personDTO));
        model.addAttribute("errorMessage", errorMessage);
        return "/persons/personEdit";
    }

    /**
     * Post метод редактирования пользователя
     */
    @PostMapping("/edit")
    public String updatePerson(@ModelAttribute PersonDTO personDTO,
                               MultipartFile file,
                               HttpServletRequest request) {
        var token = getToken(request);
        personDTO.setUpdated(Calendar.getInstance());
        var compressFile = file;
        try {
            if (!isValidFile(file)) {
                compressFile = imageCompress.compressImage(file);
            }
            personService.postUpdatePerson(token, personDTO, compressFile);
        } catch (Exception e) {
            log.error("API post {} method error: {}", getClass().getName(), e.getMessage());
            return "redirect:/persons/edit?error=true";
        }
        return "redirect:/persons/";
    }

    /**
     * Метод проверяет наличие фото у модели PhotoDTO
     *
     * @param personDTO PersontDTO
     * @return int
     */
    private int getPhotoIdByPersonDTO(PersonDTO personDTO) {
        int result = -1;
        if (personDTO.getPhoto() != null && personDTO.getPhoto().getId() > 0) {
            result = personDTO.getPhoto().getId();
        }
        return result;
    }


    private PersonDTO getPersonDTO(HttpServletRequest request) {
        var token = (String) request.getSession().getAttribute("token");
        if (token == null) {
            return null;
        }
        try {
            return personService.getPerson(token);
        } catch (Exception e) {
            log.error("PersonDTO data available. {}", e.getMessage());
            return null;
        }
    }

    /**
     * Метод проверяет загружаемый файл на содержимое и размер
     *
     * @param file MultipartFile
     * @return boolean true/false
     */
    private boolean isValidFile(MultipartFile file) {
        var maxFileSizeByte = Integer.parseInt(maxSizeFile) * 1024;
        var result = true;
        if (file == null || file.isEmpty() || file.getSize() == 0) {
            return result;
        }
        var content = Objects.requireNonNull(file.getContentType()).toLowerCase();
        if (file.getSize() > maxFileSizeByte || !this.contentTypeFile.equals(content)) {
            return !result;
        }
        return result;
    }
}
