package ru.job4j.site.controller;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.site.domain.Breadcrumb;
import ru.job4j.site.dto.Role;
import ru.job4j.site.dto.UserInfoDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 05.10.2023
 */
class RequestResponseToolsTest {

    @Test
    void whenGetTokenThenReturn123456() {
        var expect = "123456";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("token", expect);
        var actual = RequestResponseTools.getToken(request);
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenGetTokenThenNull() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        var actual = RequestResponseTools.getToken(request);
        assertThat(actual).isNull();
    }

    @Test
    void whenAddAttrBreadcrumbsThenReturnBreadcrumbs() {
        var model = new ConcurrentModel();
        var args = new String[]{"Name1", "/address1", "Name2", "/address2"};
        var expect = List.of(new Breadcrumb(args[0], args[1]), new Breadcrumb(args[2], args[3]));
        RequestResponseTools.addAttrBreadcrumbs(model, args);
        var actual = model.getAttribute("breadcrumbs");
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenAddAttrCanManageByRolesNullThenReturnFalse() {
        var model = new ConcurrentModel();
        var userInfoDto = new UserInfoDTO();
        RequestResponseTools.addAttrCanManage(model, userInfoDto);
        var actual = model.getAttribute("canManage");
        assertThat(actual).isEqualTo(false);
    }

    @Test
    void whenAddAttrCanManageByRolesNotContainRoleAdminThenReturnFalse() {
        var model = new ConcurrentModel();
        var userInfoDto = new UserInfoDTO();
        userInfoDto.setRoles(List.of(new Role(1, "ROLE_USER"), new Role(2, "ROLE_LOSER")));
        RequestResponseTools.addAttrCanManage(model, userInfoDto);
        var actual = model.getAttribute("canManage");
        assertThat(actual).isEqualTo(false);
    }

    @Test
    void whenAddAttrCanManageByRolesContainRoleAdminThenReturnTrue() {
        var model = new ConcurrentModel();
        var userInfoDto = new UserInfoDTO();
        userInfoDto.setRoles(
                List.of(
                        new Role(2, "ROLE_USER"),
                        new Role(3, "ROLE_LOSER"),
                        new Role(1, "ROLE_ADMIN")));
        RequestResponseTools.addAttrCanManage(model, userInfoDto);
        var actual = model.getAttribute("canManage");
        assertThat(actual).isEqualTo(true);
    }
}