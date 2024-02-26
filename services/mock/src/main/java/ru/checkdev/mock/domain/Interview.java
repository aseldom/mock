package ru.checkdev.mock.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity(name = "interview")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Builder(builderMethodName = "of")
@AllArgsConstructor
@NoArgsConstructor
public class Interview {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null")
    private int id;

    @Column(name = "mode")
    private int mode;

    @Column(name = "status")
    private int status;

    @JoinColumn(name = "submitter_id")
    private int submitterId;

    @NotBlank(message = "Title must be not empty")
    @JoinColumn(name = "title")
    private String title;

    @JoinColumn(name = "additional")
    private String additional;

    @NotBlank(message = "Contact must be not empty")
    @JoinColumn(name = "contact_by")
    private String contactBy;

    @NotBlank(message = "Date must be not empty")
    @JoinColumn(name = "approximate_date")
    private String approximateDate;

    @JoinColumn(name = "create_date")
    private Timestamp createDate;

    @JoinColumn(name = "topic_id")
    private Integer topicId;
}
