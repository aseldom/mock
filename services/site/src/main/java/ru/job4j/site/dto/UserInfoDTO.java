package ru.job4j.site.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Calendar;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDTO {
    private int id;
    private String username;
    private String email;
    private String key;
    private String password;
    private boolean active;
    private String experience;
    private boolean show;
    private String salary;
    private String aboutShort;
    private String about;
    private List<Role> roles;
    private boolean privacy;
    private String brief;
    private String urlHh;
    private String location;
    private Calendar updated;
}
