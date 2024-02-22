package com.example.community.institution;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvolvedInstitutionIssueTypeCount {
    private String type;
    private Long count;
}