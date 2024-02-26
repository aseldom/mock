package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.site.domain.StatusInterview;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.dto.UserInfoDTO;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.InterviewService;
import ru.job4j.site.service.TopicsService;
import ru.job4j.site.service.WisherService;

import javax.servlet.http.HttpServletRequest;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@Controller
@RequestMapping("/interview")
@AllArgsConstructor
@Slf4j
public class InterviewController {

    private final AuthService authService;
    private final TopicsService topicsService;
    private final InterviewService interviewService;
    private final WisherService wisherService;

    @GetMapping("/createForm")
    public String createForm(@ModelAttribute("topicId") int topicId,
                             Model model)
            throws JsonProcessingException {
        var topic = topicsService.getById(topicId);
        var categoryName = topic.getCategory().getName();
        int categoryId = topic.getCategory().getId();
        model.addAttribute("category", topic.getCategory());
        model.addAttribute("topic", topic);
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/index",
                "Категории", "/categories/",
                categoryName, String.format("/topics/%s/%d", categoryName, categoryId),
                topic.getName(), String.format("/topic/%s/%d/%d", categoryName, categoryId, topicId));
        return "interview/createForm";
    }

    @PostMapping("/create")
    public String createInterview(@ModelAttribute InterviewDTO interviewDTO,
                                  @ModelAttribute("topicId") int topicId,
                                  HttpServletRequest req)
            throws JsonProcessingException {
        var token = getToken(req);
        if (token != null) {
            var userInfo = authService.userInfo(token);
            interviewDTO.setSubmitterId(userInfo.getId());
        }
        interviewDTO.setTopicId(topicId);
        InterviewDTO createInterview = interviewService.create(getToken(req), interviewDTO);
        return "redirect:/interview/" + createInterview.getId();
    }

    @GetMapping("/{interviewId}")
    public String details(@PathVariable("interviewId") int interviewId,
                          Model model,
                          HttpServletRequest req) throws JsonProcessingException {
        var token = getToken(req);
        var interview = interviewService.getById(token, interviewId);
        var userInfo = authService.userInfo(token);
        var isAuthor = interviewService.isAuthor(userInfo, interview);
        var wishers = wisherService.getAllWisherDtoByInterviewId(token, String.valueOf(interview.getId()));
        var isWisher = wisherService.isWisher(userInfo.getId(), interview.getId(), wishers);
        var statisticMap = wisherService.getInterviewStatistic(wishers);
        model.addAttribute("interview", interview);
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("isWisher", isWisher);
        model.addAttribute("statisticMap", statisticMap);
        model.addAttribute("statuses", StatusInterview.values());
        model.addAttribute("STATUS_IN_PROGRESS_ID", StatusInterview.IN_PROGRESS.getId());
        model.addAttribute("STATUS_IS_FEEDBACK_ID", StatusInterview.IS_FEEDBACK.getId());
        if (isAuthor) {
            var wishersDetail = interviewService.getAllWisherDetail(wishers);
            boolean isDismissed = wisherService.isDismissed(interviewId, wishers);
            model.addAttribute("isDismissed", isDismissed);
            model.addAttribute("wishersDetail", wishersDetail);
        }

        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/index",
                "Собеседования", "/interviews/",
                interview.getTitle(), String.format("/interview/%d", interviewId));
        return "interview/details";
    }

    /**
     * Метод отображает страницу редактирования интервью (собеседования).
     * Для редактирования собеседования авторизованный пользователь должен быть создателем интервью(собеседования)
     *
     * @param interviewId int
     * @param model       Model
     * @param request     HttpServletRequest
     * @return String view page
     */
    @GetMapping("/edit/{id}")
    public String getEditView(@PathVariable("id") int interviewId,
                              Model model,
                              HttpServletRequest request) {
        var token = getToken(request);
        InterviewDTO interview;
        UserInfoDTO userInfoDTO;
        try {
            userInfoDTO = authService.userInfo(token);
            interview = interviewService.getById(token, interviewId);
            if (interview.getSubmitterId() != userInfoDTO.getId()) {
                return "redirect:/interview/" + interviewId;
            }
        } catch (Exception e) {
            log.error("Remote application not responding. Error: {}. {}, ", e.getCause(), e.getMessage());
            return "redirect:/interviews/";
        }
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/index",
                "Собеседования", "/interviews/",
                interview.getTitle(), String.format("/interview/edit/%d", interviewId));
        model.addAttribute("interview", interview);
        return "interview/interviewEdit";
    }

    /**
     * Метод обновления собеседования
     *
     * @param interview InterviewDTO.class
     * @param request   HttpServletRequest
     * @return String redirect page
     */
    @PostMapping("/update")
    public String postUpdateInterview(@ModelAttribute InterviewDTO interview,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        var token = getToken(request);
        try {
            interviewService.update(token, interview);
        } catch (Exception e) {
            log.error("Remote application not responding. Error, {}. {}, ", e.getCause(), e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Собеседование не обновлено");
            return "redirect:/interview/edit/" + interview.getId();
        }
        return "redirect:/interview/" + interview.getId();
    }

    @GetMapping("/{interviewId}/participate")
    public String participate(@PathVariable("interviewId") int interviewId,
                              Model model,
                              HttpServletRequest req) throws JsonProcessingException {
        var token = getToken(req);
        var userInfoDTO = authService.userInfo(token);
        var interview = interviewService.getById(token, interviewId);
        var result = "interview/participate";
        if (userInfoDTO != null && interview.getSubmitterId() != userInfoDTO.getId()) {
            var wishers = wisherService.getAllWisherDtoByInterviewId(token, String.valueOf(interview.getId()));
            var statisticMap = wisherService.getInterviewStatistic(wishers);
            model.addAttribute("interview", interview);
            model.addAttribute("statisticMap", statisticMap);
            RequestResponseTools.addAttrBreadcrumbs(model,
                    "Главная", "/index",
                    "Собеседования", "/interviews/",
                    interview.getTitle(), String.format("/interview/%d", interviewId),
                    "принять участие в собеседовании", "/participate");
        } else {
            result = String.format("redirect:/interview/%d", interviewId);
        }
        return result;
    }
}
