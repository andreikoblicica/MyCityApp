package com.example.community.issue;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IssueStatusCount {
    private String status;
    private Long count;
}
