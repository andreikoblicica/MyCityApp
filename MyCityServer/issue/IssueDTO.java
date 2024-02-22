package com.example.community.issue;

import com.example.community.institution.InvolvedInstitutionDTO;

import com.example.community.message.MessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueDTO {

    private Long id;
    private String username;
    private String type;
    private List<InvolvedInstitutionDTO> involvedInstitutions;
    private String title;
    private String description;
    private String location;
    private String coordinates;
    private String dateTime;
    private String image;
    private String status;
    private List<MessageDTO> messages;

    public IssueDTO(String username, String type, String title, String description, String location, String coordinates, String image, String status) {
        this.username = username;
        this.type = type;
        this.title = title;
        this.description = description;
        this.location = location;
        this.coordinates = coordinates;
        this.image = image;
        this.status = status;
        this.messages=new ArrayList<>();
    }
}
