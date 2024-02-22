package com.example.community.analytics;

import com.example.community.event.EventStatusCount;
import com.example.community.event.EventTypeCount;
import com.example.community.issue.IssueStatusCount;
import com.example.community.issue.IssueTypeCount;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.List;

@Data
@AllArgsConstructor
public class Analytics {
    private Long institutionUsers;
    private Long regularUsers;
    private Long institutions;
    private Long issues;
    private Long facilities;
    private Long events;
    private Long news;
    private Long alerts;
    private List<IssueStatusCount> issueStatuses;
    private List<EventStatusCount> eventStatuses;
    private List<IssueTypeCount> issueTypes;
    private List<EventTypeCount> eventTypes;

}
