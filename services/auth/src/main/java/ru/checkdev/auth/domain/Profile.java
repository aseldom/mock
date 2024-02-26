package ru.checkdev.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

/**
 * @author parsentev
 * @since 25.09.2016
 */
@Entity(name = "profile")
public class Profile {
    @Transient
    private final StandardPasswordEncoder encoding = new StandardPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    @Column(unique = true)
    private String email;

    private String key;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean active;

    private String experience;

    private boolean show;

    private String salary;
    @Column(name = "about_short")
    private String aboutShort;

    private String about;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id", name = "id_photo")
    private Photo photo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "profile_role",
            joinColumns = {
                    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", nullable = false, updatable = false)
            }
    )
    private List<Role> roles;

    /**
     * Privacy sign.
     */
    private boolean privacy;

    @Transient
    private String brief;

    @Transient
    private String urlHh;

    private String location;

    private Calendar updated;

    private Calendar created;

    public Profile() {
    }

    public Profile(String username, String email, String password) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Profile(String username, String experience, String salary, String aboutShort, String about, String location) {
        this.experience = experience;
        this.salary = salary;
        this.aboutShort = aboutShort;
        this.about = about;
        this.username = username;
        this.location = location;
    }

    public Profile(String key, String experience, String salary, String aboutShort, String username, String location, Object photo) {
        this.key = key;
        this.experience = experience;
        this.salary = salary;
        this.aboutShort = aboutShort;
        this.username = username;
        this.location = location;
        if (photo != null) {
            this.photo = new Photo((Integer) photo);
        }
    }

    public Profile(int id, String username, String email, String key, String password, boolean active, String experience,
                   boolean show, String salary, String aboutShort, String about, Photo photo, boolean privacy, String location) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.key = key;
        this.password = password;
        this.active = active;
        this.experience = experience;
        this.show = show;
        this.salary = salary;
        this.aboutShort = aboutShort;
        this.about = about;
        this.photo = photo;
        this.privacy = privacy;
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAboutShort() {
        return aboutShort;
    }

    public void setAboutShort(String aboutShort) {
        this.aboutShort = aboutShort;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getUrlHh() {
        return urlHh;
    }

    public void setUrlHh(String urlHh) {
        this.urlHh = urlHh;
    }

    /**
     * Return privacy sign.
     *
     * @return privacy sign.
     */
    public boolean isPrivacy() {
        return privacy;
    }

    /**
     * Set privacy sign.
     *
     * @param privacy privacy sign.
     */
    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }


    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        return id == profile.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Calendar getUpdated() {
        return updated;
    }

    public void setUpdated(Calendar updated) {
        this.updated = updated;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Password {
        private int id;
        private String password;
        private String newPass;

        public Password(int id, String password, String newPass) {
            this.id = id;
            this.password = password;
            this.newPass = newPass;
        }

        public Password() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNewPass() {
            return newPass;
        }

        public void setNewPass(String newPass) {
            this.newPass = newPass;
        }
    }
}
