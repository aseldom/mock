package ru.job4j.site.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.site.domain.StatusInterview;
import ru.job4j.site.domain.StatusWisher;
import ru.job4j.site.dto.WisherDto;
import ru.job4j.site.service.InterviewService;
import ru.job4j.site.service.WisherService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Контроллер обработки запросов на подписку на собеседование.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.10.2023
 */
@Controller
@RequestMapping("/wisher")
@AllArgsConstructor
public class WisherController {
    private final WisherService wisherService;
    private final InterviewService interviewService;

    /**
     * Подать заявку на участие в собеседовании.
     *
     * @param wishParam Map<String, String>
     * @param request   HttpServletRequest
     * @return String page.
     */
    @PostMapping("/create")
    public String createWisher(@RequestParam Map<String, String> wishParam,
                               HttpServletRequest request) {
        var token = RequestResponseTools.getToken(request);
        int interviewId = Integer.parseInt(wishParam.get("interviewId"));
        int userId = Integer.parseInt(wishParam.get("userId"));
        var contactBy = wishParam.get("contactBy");
        var wisherDto = new WisherDto(0, interviewId, userId, contactBy, false, StatusWisher.IS_CONSIDERED.getId());
        wisherService.saveWisherDto(token, wisherDto);
        return "redirect:/interview/" + interviewId;
    }

    /**
     * Одобрить участника собеседование, а остальных участников отклонить
     *
     * @param param   Map<String, String>
     * @param request HttpServletRequest
     * @return String page.
     */
    @PostMapping("/dismissed")
    public String dismissedWisher(@RequestParam Map<String, String> param,
                                  HttpServletRequest request) {
        var token = RequestResponseTools.getToken(request);
        var statusDismissedID = String.valueOf(StatusWisher.IS_DISMISSED.getId());
        var statusRejectedID = String.valueOf(StatusWisher.IS_REJECTED.getId());
        var interviewId = param.get("interviewId");
        var wisherId = param.get("wisherId");
        wisherService.setNewStatusByWisherInterview(
                token, interviewId, wisherId, statusDismissedID, statusRejectedID);
        interviewService.updateStatus(token, Integer.parseInt(interviewId), StatusInterview.IN_PROGRESS.getId());
        return "redirect:/interview/" + interviewId;
    }
}
