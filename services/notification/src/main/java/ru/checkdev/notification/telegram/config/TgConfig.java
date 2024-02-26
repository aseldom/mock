package ru.checkdev.notification.telegram.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 3. Мидл
 * Класс дополнительных функций телеграм бота, проверка почты, генерация пароля.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.09.2023
 */
public class TgConfig {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}");
    private final String prefix;
    private final int passSize;

    public TgConfig(String prefix, int passSize) {
        this.prefix = prefix;
        this.passSize = passSize;
    }

    /**
     * Метод проверяет входящую строку на соответствие формату email
     *
     * @param email String
     * @return boolean
     */
    public boolean isEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    /**
     * метод генерирует пароль для пользователя
     *
     * @return String
     */
    public String getPassword() {
        String password = prefix + UUID.randomUUID();
        return password.substring(0, passSize);
    }

    /**
     * Метод преобразовывает Object в карту Map<String,String>
     *
     * @param object Object or Person(Auth)
     * @return Map
     */
    public Map<String, String> getObjectToMap(Object object) {
        return MAPPER.convertValue(object, Map.class);
    }
}
