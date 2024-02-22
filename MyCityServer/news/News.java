package com.example.community.news;


import com.example.community.institution.Institution;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="institution_id")
    private Institution institution;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private LocalDateTime dateTime;

    @Column
    private String image;

    @Column
    private String websiteLink;

    public News(String title, String description, LocalDateTime dateTime, String image, String websiteLink) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.image = image;
        this.websiteLink = websiteLink;
    }
}
