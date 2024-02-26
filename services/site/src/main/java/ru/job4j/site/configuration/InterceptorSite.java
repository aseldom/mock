package ru.job4j.site.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.site.dto.UserInfoDTO;
import ru.job4j.site.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CheckDev пробное собеседование
 * InterceptorSite Глобальный перехватчик для добавления во все виды модели UserInfo
 *
 * @author Dmitry Stepanov
 * @version 24.09.2023 15:15
 */
@Component
@AllArgsConstructor
@Slf4j
public class InterceptorSite implements HandlerInterceptor {
    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        var userInfo = getUserInfo(request);
        if (modelAndView != null) {
            modelAndView.addObject("userInfo", userInfo);
        }
    }

    private UserInfoDTO getUserInfo(HttpServletRequest request) {
        var token = (String) request.getSession().getAttribute("token");
        if (token == null) {
            return null;
        }
        try {
            return authService.userInfo(token);
        } catch (Exception e) {
            log.error("UserInfo data available. {}", e.getMessage());
            return null;
        }
    }
}
