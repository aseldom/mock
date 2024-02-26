package ru.checkdev.auth.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Calendar;

/**
 * Created by Mikhail Epatko.
 * 24 August 2017
 * mail: mikhail.epatko@gmail.com
 */
@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = -1;
    private int userId = -1;
    private String email;
    private String username;
    private String msg;
    private Calendar created;
    private boolean archive;

    public Order() {
    }

    public Order(int id, int userId, String email, String username, String msg, Calendar created, boolean archive) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.msg = msg;
        this.created = created;
        this.archive = archive;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return id == order.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
