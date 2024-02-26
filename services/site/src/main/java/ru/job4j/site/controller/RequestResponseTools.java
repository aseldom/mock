package ru.job4j.site.controller;

import org.springframework.ui.Model;
import ru.job4j.site.domain.Breadcrumb;
import ru.job4j.site.dto.UserInfoDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;

public class RequestResponseTools {
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String TOKEN_ATTR = "token";

    public static String getToken(HttpServletRequest request) {
        return (String) request.getSession().getAttribute(TOKEN_ATTR);
    }

    public static void addAttrBreadcrumbs(Model model, String... args) {
        var list = new ArrayList<Breadcrumb>();
        for (int i = 0; i < args.length; i += 2) {
            list.add(new Breadcrumb(args[i], args[i + 1]));
        }
        model.addAttribute("breadcrumbs", list);
    }

    public static void addAttrCanManage(Model model, UserInfoDTO userInfo) {
        var roles = userInfo.getRoles();
        boolean canManage = false;
        if (Objects.nonNull(roles)) {
            canManage = roles.stream()
                    .anyMatch(role -> role.getValue().equals(ROLE_ADMIN));
        }
        model.addAttribute("canManage", canManage);
    }
}
