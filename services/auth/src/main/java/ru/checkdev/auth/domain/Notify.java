package ru.checkdev.auth.domain;

import java.util.Map;
import java.util.Objects;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Notify {
    private String email;
    private Map<String, ?> keys;
    private String template;

    public Notify(String email, Map<String, ?> keys, String template) {
        this.email = email;
        this.keys = keys;
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, ?> getKeys() {
        return keys;
    }

    public void setKeys(Map<String, ?> keys) {
        this.keys = keys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notify notify = (Notify) o;

        return Objects.equals(template, notify.template);
    }

    @Override
    public int hashCode() {
        return template != null ? template.hashCode() : 0;
    }

    public enum Type {
        REG, FORGOT, ORDER
    }

}
