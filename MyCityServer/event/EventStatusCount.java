package com.example.community.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventStatusCount {
    private String status;
    private Long count;
}
