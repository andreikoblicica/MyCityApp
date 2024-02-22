package com.example.community.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventTypeCount {
    private String type;
    private Long count;
}
