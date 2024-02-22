package com.example.community.event;


import com.example.community.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;


import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column
    private String type;

    @Column
    private String title;

    @Column
    private String image;


    @Column
    private LocalDateTime startDateTime;

    @Column
    private LocalDateTime endDateTime;

    @Column
    private String description;

    @Column
    private String location;

    @Column
    private String status;


    @Column
    private Long favoriteCount;


    public Event(String eventType, String title, String image, LocalDateTime startDateTime, LocalDateTime endDateTime, String description, String location, String status) {
        this.type = eventType;
        this.title = title;
        this.image = image;
        this.startDateTime = startDateTime;
        this.endDateTime=endDateTime;
        this.description = description;
        this.location = location;
        this.status = status;
        this.favoriteCount= 0L;
    }
}
