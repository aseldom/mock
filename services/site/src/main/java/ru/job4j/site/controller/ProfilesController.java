package ru.job4j.site.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.site.service.ProfilesService;

/**
 * CheckDev пробное собеседование
 * ProfilesController класс обработки запросов профилей.
 *
 * @author Dmitry Stepanov
 * @version 23.04.2023T10:31
 */
@Controller
@RequestMapping("/profiles")
@Slf4j
public class ProfilesController {
    private final ProfilesService profilesService;

    public ProfilesController(ProfilesService profilesService) {
        this.profilesService = profilesService;
    }

    /**
     * Отображение вида одного ProfileDTO
     *
     * @param id    ID Profile
     * @param model Model
     * @return String "oneProfile" page
     */
    @GetMapping("/{id}")
    public String getProfileById(@PathVariable int id, Model model) {
        String username = "";
        var profileOptional = profilesService.getProfileById(id);
        if (profileOptional.isPresent()) {
            model.addAttribute("profile", profileOptional.get());
            username = profileOptional.get().getUsername();
        }
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/",
                "Профили", "/profiles/",
                username, "/profiles/" + id
        );
        return "/profiles/profileView";
    }

    /**
     * Отображение вида всех профилей List<ProfileDTO>
     *
     * @param model Model
     * @return String "/profiles" pge
     */
    @GetMapping("/")
    public String getAllProfiles(Model model) {
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/",
                "Профили", "/profiles/"
        );
        var profilesList = profilesService.getAllProfile();
        model.addAttribute("profiles", profilesList);
        model.addAttribute("current_page", "profiles");
        return "profiles/profiles";
    }
}
