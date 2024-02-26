package ru.checkdev.notification.telegram.config;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.PersonDTO;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing TgConfig;
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 06.10.2023
 */
class TgConfigTest {
    private final String prefix = "pr/";
    private final int passSize = 10;
    private final TgConfig tgConfig = new TgConfig(prefix, passSize);

    @Test
    void whenIsEmailThenReturnTrue() {
        var email = "mail@mail.ru";
        var actual = tgConfig.isEmail(email);
        assertThat(actual).isTrue();
    }

    @Test
    void whenIsEmailThenReturnFalse() {
        var email = "mail.ru";
        var actual = tgConfig.isEmail(email);
        assertThat(actual).isFalse();
    }

    @Test
    void whenGetPasswordThenLengthPassSize() {
        var pass = tgConfig.getPassword();
        assertThat(pass.length()).isEqualTo(passSize);
    }

    @Test
    void whenGetPasswordThenStartWishPrefix() {
        var pass = tgConfig.getPassword();
        assertThat(pass.startsWith(prefix)).isTrue();
    }

    @Test
    void whenGetObjectToMapThenReturnObjectMap() {
        var personDto = new PersonDTO("mail", "pass", true, null, Calendar.getInstance());
        var map = tgConfig.getObjectToMap(personDto);
        assertThat(map.get("email")).isEqualTo(personDto.getEmail());
        assertThat(map.get("password")).isEqualTo(personDto.getPassword());
        assertThat(String.valueOf(map.get("privacy"))).isEqualTo(String.valueOf(true));
    }
}