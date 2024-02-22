package com.example.community.issue;

import com.example.community.institution.InstitutionBuilder;
import com.example.community.institution.InvolvedInstitutionDTO;
import com.example.community.message.MessageBuilder;
import com.example.community.message.MessageDTO;
import com.example.community.user.User;
import com.example.community.user.UserRepository;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class IssueBuilder {


    public static IssueDTO toDTO(Issue issue) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", Locale.ENGLISH);
        List<InvolvedInstitutionDTO> involvedInstitutionDTOS = issue.getInvolvedInstitutions().stream().map(InstitutionBuilder::toDTO).collect(Collectors.toCollection(ArrayList::new));
        List<MessageDTO> messageDTOS = issue.getMessages().stream().map(MessageBuilder::toDTO).collect(Collectors.toCollection(ArrayList::new));
        System.out.println(messageDTOS);
        return new IssueDTO(
                issue.getId(),
                issue.getUser().getUsername(),
                issue.getType(),
                involvedInstitutionDTOS,
                issue.getTitle(),
                issue.getDescription(),
                issue.getLocation(),
                issue.getCoordinates(),
                issue.getDateTime().format(formatter),
                issue.getImage(),
                issue.getStatus(),
                messageDTOS
                );
    }

    public static IssueDTO toNewDTO(ReportedIssueDTO issue) {
        return new IssueDTO(
               issue.getUsername(),
                issue.getType(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getLocation(),
                issue.getCoordinates(),
                issue.getImage(),
                "Opened"
        );
    }


    public static Issue toEntity(IssueDTO issueDTO, UserRepository userRepository) {
        User user=userRepository.findByUsername(issueDTO.getUsername()).orElse(null);
        Issue issue=new Issue( issueDTO.getType(),
                issueDTO.getTitle(),
                issueDTO.getDescription(),
                issueDTO.getLocation(),
                issueDTO.getCoordinates(),
                issueDTO.getImage(),
                issueDTO.getStatus());
        issue.setUser(user);
        return issue;
    }
}
