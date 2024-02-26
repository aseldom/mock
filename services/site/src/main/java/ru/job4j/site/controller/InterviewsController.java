package ru.job4j.site.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.site.domain.StatusInterview;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.dto.ProfileDTO;
import ru.job4j.site.dto.TopicIdNameDTO;
import ru.job4j.site.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@Controller
@RequestMapping("/interviews")
@Slf4j
public class InterviewsController {

    private final InterviewsService interviewsService;
    private final ProfilesService profilesService;
    private final CategoriesService categoriesService;
    private final TopicsService topicsService;
    private final AuthService authService;
    private final FilterService filterService;
    private final WisherService wisherService;

    public InterviewsController(InterviewsService interviewsService, ProfilesService profilesService,
                                CategoriesService categoriesService, TopicsService topicsService,
                                AuthService authService, FilterService filterService,
                                WisherService wisherService) {
        this.interviewsService = interviewsService;
        this.profilesService = profilesService;
        this.categoriesService = categoriesService;
        this.topicsService = topicsService;
        this.authService = authService;
        this.filterService = filterService;
        this.wisherService = wisherService;
    }

    @GetMapping("/")
    public String getAllInterviews(Model model,
                                   HttpServletRequest req,
                                   @RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "20") int size) {
        try {
            var token = getToken(req);
            var user = authService.userInfo(token);
            var userId = user != null ? user.getId() : 0;
            var filter = userId > 0
                    ? filterService.getByUserId(token, userId) : null;
            var isFiltered = filter != null && filter.getCategoryId() > 0;
            Page<InterviewDTO> interviewsPage;
            List<TopicIdNameDTO> topicIdNameDTOS = new ArrayList<>();
            var categoryName = "";
            var topicName = "";
            var categories = categoriesService.getAll();
            if (isFiltered) {
                topicIdNameDTOS = topicsService.getTopicIdNameDtoByCategory(filter.getCategoryId());
                interviewsPage = filter.getTopicId() > 0
                        ? interviewsService.getByTopicId(filter.getTopicId(), page, size)
                        : interviewsService.getByTopicsIds(
                        topicIdNameDTOS.stream().map(TopicIdNameDTO::getId).toList(), page, size);
                categoryName = categoriesService.getNameById(categories, filter.getCategoryId());
                topicName = filter.getTopicId() > 0 ? topicsService.getNameById(filter.getTopicId()) : "";
            } else {
                interviewsPage = interviewsService.getAll(token, page, size);
            }
            Set<ProfileDTO> userList = interviewsPage.toList().stream()
                    .map(x -> profilesService.getProfileById(x.getSubmitterId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            var wishers = wisherService.getAllWisherDtoByInterviewId(token, "");
            var interviewStatistic = wisherService.getInterviewStatistic(wishers);
            RequestResponseTools.addAttrBreadcrumbs(model,
                    "Главная", "/index",
                    "Собеседования", String.format("/interviews/?page=%d&?size=%d", page, size)
            );
            model.addAttribute("statisticMap", interviewStatistic);
            model.addAttribute("interviewsPage", interviewsPage);
            model.addAttribute("statuses", StatusInterview.values());
            model.addAttribute("current_page", "interviews");
            model.addAttribute("users", userList);
            model.addAttribute("categories", categories);
            model.addAttribute("filter", filter);
            model.addAttribute("userId", userId);
            model.addAttribute("categoryName", categoryName);
            model.addAttribute("topicName", topicName);
            model.addAttribute("topics", topicIdNameDTOS);
        } catch (Exception e) {
            RequestResponseTools.addAttrBreadcrumbs(model,
                    "Главная", "/index",
                    "Собеседования", String.format("/interviews/?page=%d&?size=%d", page, size)
            );
            log.error("Remote application not responding. Error: {}. {}, ", e.getCause(), e.getMessage());
        }
        return "interview/interviews";
    }
}
