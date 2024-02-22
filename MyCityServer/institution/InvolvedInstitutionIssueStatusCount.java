package com.example.community.institution;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvolvedInstitutionIssueStatusCount {
    private String status;
    private Long count;
}
