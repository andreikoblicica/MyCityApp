package com.example.community.issue;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportedIssueDTO {

    private String username;
    private String type;
    private String title;
    private String description;
    private String location;
    private String coordinates;
    private String image;
}

