package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.CategoriesService;
import ru.job4j.site.service.NotificationService;
import ru.job4j.site.service.TopicsService;

import javax.servlet.http.HttpServletRequest;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@Controller
@RequestMapping("/topics")
@AllArgsConstructor
@Slf4j
public class TopicsControl {
    private final TopicsService topicsService;
    private final AuthService authService;
    private final CategoriesService categoriesService;

    private final NotificationService notifications;

    @GetMapping("/{categoryId}")
    public String getByCategory(@PathVariable int categoryId,
                                Model model,
                                HttpServletRequest req) throws JsonProcessingException {
        var topics = topicsService.getByCategory(categoryId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("topics", topics);
        String categoryName = topics.isEmpty() ? "" : topics.get(0).getCategory().getName();
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/index",
                "Категории", "/categories/",
                categoryName, String.format("/topics/%d", categoryId));
        try {
            var token = getToken(req);
            if (token != null) {
                var userInfo = authService.userInfo(token);
                model.addAttribute("userInfo", userInfo);
                RequestResponseTools.addAttrCanManage(model, userInfo);
                categoriesService.updateStatistic(token, categoryId);
                model.addAttribute("userTopicDTO", notifications.findTopicByUserId(userInfo.getId()));
            }
        } catch (Exception e) {
            RequestResponseTools.addAttrBreadcrumbs(model,
                    "Главная", "/index",
                    "Категории", "/categories/",
                    categoryName, String.format("/topics/%d", categoryId));
            log.error("Remote application not responding. Error: {}. {}, ", e.getCause(), e.getMessage());
        }
        return "topic/topics";
    }
}