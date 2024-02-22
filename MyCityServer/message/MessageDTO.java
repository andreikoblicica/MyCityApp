package com.example.community.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageDTO {
    private Long issueId;
    private String sourceName;
    private List<String> destinationNames;
    private String message;
    private String timestamp;
}