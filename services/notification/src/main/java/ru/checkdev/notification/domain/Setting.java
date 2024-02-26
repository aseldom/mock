package ru.checkdev.notification.domain;

import javax.persistence.*;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "setting")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.ORDINAL)
    private Key key;

    private String value;

    public Setting() {
    }

    public Setting(Key key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public enum Key {
        HOST, PORT, AUTH, FROM, USERNAME, PASSWORD
    }
}
