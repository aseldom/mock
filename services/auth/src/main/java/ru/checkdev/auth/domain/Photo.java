package ru.checkdev.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Hincu Andrei on 27.05.2018.
 * @version $Id$.
 * @since 0.1.
 */
@Entity(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonIgnore
    private byte[] photo;

    private String name;


    public Photo() {
    }

    public Photo(int id) {
        this.id = id;
    }

    public Photo(byte[] photo, String name) {
        this.photo = photo;
        this.name = name;
    }

    public Photo(int id, byte[] photo, String name) {
        this.id = id;
        this.photo = photo;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
