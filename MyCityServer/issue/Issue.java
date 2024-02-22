package com.example.community.issue;



import com.example.community.institution.InvolvedInstitution;
import com.example.community.message.Message;
import com.example.community.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "issue", fetch = FetchType.EAGER)
    private List<InvolvedInstitution> involvedInstitutions;

    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String location;

    @Column
    private String coordinates;
    @Column
    private LocalDateTime dateTime;
    @Column
    private String image;
    @Column
    private String status;

    @Column
    private String type;

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY)
    private List<Message> messages;

    public Issue(String type,String title, String description, String location,String coordinates, String image, String status) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.image = image;
        this.status = status;
        this.type=type;
        this.coordinates=coordinates;
        this.involvedInstitutions =new ArrayList<>();
        this.messages =new ArrayList<>();
    }
}
