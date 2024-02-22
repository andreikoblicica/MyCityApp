package com.example.community.message;

import com.example.community.issue.Issue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Message {

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sourceName;

    @ElementCollection
    private List<String> destinationNames;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String timestamp;

    @ManyToOne
    @JoinColumn(name="issue_id")
    private Issue issue;


    public Message(String sourceName, List<String> destinationNames, String message, String timestamp) {
        this.sourceName = sourceName;
        this.destinationNames = destinationNames;
        this.message = message;
        this.timestamp = timestamp;
    }
}