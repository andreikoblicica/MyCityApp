package com.example.community.issue;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IssueTypeCount {
    private String type;
    private Long count;
}
