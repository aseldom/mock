package ru.job4j.site.domain;

import lombok.Data;

import java.util.Objects;

@Data
public class Breadcrumb {

    private String name;
    private String url;

    public Breadcrumb(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Breadcrumb that)) {
            return false;
        }
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name) * 31;
    }
}