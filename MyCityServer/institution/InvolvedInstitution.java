package com.example.community.institution;


import com.example.community.issue.Issue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class InvolvedInstitution {

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="issue_id")
    private Issue issue;

    @Column
    private String involvedInstitutionName;

    @Column
    private String involvedUserName;

    @Column
    private String status;

    public InvolvedInstitution(String involvedInstitutionName) {
        this.involvedInstitutionName = involvedInstitutionName;
        this.status="Opened";
    }


}
