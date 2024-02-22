package com.example.community.institution;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvolvedInstitutionDTO {
    private Long id;
    private String institutionName;
    private String username;
    private String issueStatus;
}
