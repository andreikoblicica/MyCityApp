package com.example.community.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAnalytics {
    private Long issues;
    private Long events;
}
