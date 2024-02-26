package ru.job4j.site.service;

import org.junit.jupiter.api.Test;
import ru.job4j.site.domain.StatusWisher;
import ru.job4j.site.dto.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * CheckDev пробное собеседование
 *
 * @author Dmitry Stepanov
 * @version 21.10.2023 23:28
 */
class InterviewServiceTest {
    private ProfilesService profilesService = mock(ProfilesService.class);
    private InterviewService interviewService = new InterviewService(profilesService);

    @Test
    void injectedNotNull() {
        assertThat(interviewService).isNotNull();
    }

    @Test
    void whenIsAuthorThenTrue() {
        var user = new UserInfoDTO();
        user.setId(1);
        var interview = new InterviewDTO();
        interview.setSubmitterId(user.getId());
        var actual = interviewService.isAuthor(user, interview);
        assertThat(actual).isTrue();
    }

    @Test
    void whenIsAuthorThenFalse() {
        var user = new UserInfoDTO();
        user.setId(1);
        var interview = new InterviewDTO();
        interview.setSubmitterId(55);
        var actual = interviewService.isAuthor(user, interview);
        assertThat(actual).isFalse();
    }

    @Test
    void getAllWisherDetail() {
        var person = new ProfileDTO();
        person.setId(1);
        person.setUsername("name");
        var wisher1 = new WisherDto(1, 1, person.getId(), "mail1", false, 1);
        var wisher2 = new WisherDto(2, 1, person.getId(), "mail2", false, 1);
        var wishers = List.of(wisher1, wisher2);
        when(profilesService.getProfileById(person.getId())).thenReturn(Optional.of(person));
        var wisherDetail1 = new WisherDetailDTO(wisher1.getId(), wisher1.getInterviewId(), wisher1.getUserId(),
                person.getUsername(), wisher1.getContactBy(), wisher1.isApprove(), wisher1.getStatus(),
                StatusWisher.IS_CONSIDERED.getInfo());
        var wisherDetail2 = new WisherDetailDTO(wisher2.getId(), wisher2.getInterviewId(), wisher2.getUserId(),
                person.getUsername(), wisher2.getContactBy(), wisher2.isApprove(), wisher2.getStatus(),
                StatusWisher.IS_CONSIDERED.getInfo());
        var expected = List.of(wisherDetail1, wisherDetail2);
        var actual = interviewService.getAllWisherDetail(wishers);
        assertThat(actual).isEqualTo(expected);
    }
}