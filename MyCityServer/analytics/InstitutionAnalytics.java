package com.example.community.analytics;

import com.example.community.institution.InvolvedInstitutionIssueStatusCount;
import com.example.community.institution.InvolvedInstitutionIssueTypeCount;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InstitutionAnalytics {
    private Long news;
    private Long alerts;
    private Long issues;
    private List<InvolvedInstitutionIssueStatusCount> issueStatuses;
    private List<InvolvedInstitutionIssueTypeCount> issueTypes;
}
