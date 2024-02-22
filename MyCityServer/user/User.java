package com.example.community.user;

import com.example.community.event.Event;
import com.example.community.institution.Institution;
import com.example.community.issue.Issue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String name;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private String role;

    //institution users
    @ManyToOne
    @JoinColumn(name="institution_id")
    private Institution institution;

    //regular users
    @OneToMany(mappedBy="user",fetch = FetchType.LAZY)
    private List<Event> events;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "favorite_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> favorites;


    public User(String username,String name, String password, String email, String role) {
        this.username = username;
        this.name=name;
        this.password = password;
        this.email = email;
        this.role = role;
        favorites=new ArrayList<>();
    }

    public User(String username,String name, String password, String email, String role,Institution institution) {
        this.username = username;
        this.name=name;
        this.password = password;
        this.email = email;
        this.role = role;
        favorites=new ArrayList<>();
        this.institution=institution;
    }

    @OneToMany(mappedBy="user",fetch = FetchType.LAZY)
    private List<Issue> issues;
}
