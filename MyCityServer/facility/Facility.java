package com.example.community.facility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;



import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Facility {

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String type;

    @Column
    private String name;

    @Column
    private String location;

    @Column
    private String description;

    @Column
    private String websiteLink;


    private String imageUrl;


    public Facility(String type, String name, String location, String description, String websiteLink, String imageUrl) {
        this.type = type;
        this.name = name;
        this.location = location;
        this.description = description;
        this.websiteLink = websiteLink;
        this.imageUrl = imageUrl;
    }
}
