package com.example.community.institution;

import com.example.community.alert.Alert;
import com.example.community.news.News;
import com.example.community.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Institution {
    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String address;



    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private String website;

    @OneToMany(mappedBy = "institution")
    private List<User> users;


    @OneToMany(mappedBy = "institution")
    private List<News> news;

    @OneToMany(mappedBy = "institution")
    private List<Alert> alerts;

    @Column
    private String image;


    public Institution(String name, String address, String email, String phoneNumber, String website,String image) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.image=image;
    }
}
