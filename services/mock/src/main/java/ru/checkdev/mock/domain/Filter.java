package ru.checkdev.mock.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "cd_filter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Filter {

    @Id
    @Column(name = "user_id", nullable = false)
    private int userId;
    @JoinColumn(name = "category_id")
    private int categoryId;
    @JoinColumn(name = "topic_id")
    private int topicId;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Filter filter)) {
            return false;
        }
        return userId == filter.userId
                && categoryId == filter.categoryId && topicId == filter.topicId;
    }

    @Override
    public int hashCode() {
        return (((((Objects.hash(userId)) * 31)
                + Objects.hash(categoryId)) * 31) + Objects.hash(topicId)) * 31;
    }
}
