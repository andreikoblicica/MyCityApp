package com.example.community.issue;

import com.example.community.institution.*;
import com.example.community.message.Message;
import com.example.community.message.MessageBuilder;
import com.example.community.message.MessageDTO;
import com.example.community.message.MessageRepository;

import com.example.community.user.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Transactional
@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;

    private final InvolvedInstitutionRepository involvedInstitutionRepository;

    private final MessageRepository messageRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository, UserRepository userRepository, InstitutionRepository institutionRepository, InvolvedInstitutionRepository involvedInstitutionRepository, MessageRepository messageRepository) {
       this.issueRepository=issueRepository;
       this.userRepository=userRepository;
       this.institutionRepository=institutionRepository;
       this.involvedInstitutionRepository=involvedInstitutionRepository;
       this.messageRepository=messageRepository;
    }

    public IssueDTO findById(Long id) {
        Optional<Issue> issue=issueRepository.findById(id);
        return issue.map(IssueBuilder::toDTO).orElse(null);
    }

    public List<IssueDTO> findAll() {
        List<Issue> issues = issueRepository.findAll();
        return issues.stream().map(IssueBuilder::toDTO).collect(Collectors.toList());
    }

    public List<IssueDTO> findByUserUsername(String username) {
        List<Issue> issueList = issueRepository.findByUserUsername(username);
        return issueList.stream()
                .map(IssueBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public IssueDTO create(ReportedIssueDTO reportedIssueDTO) {
        IssueDTO issueDTO=IssueBuilder.toNewDTO(reportedIssueDTO);
        Issue issueInitial=IssueBuilder.toEntity(issueDTO,userRepository);
        issueInitial.setDateTime(LocalDateTime.now());
        Issue issue= issueRepository.save(issueInitial);
        InvolvedInstitution newInvolvedInstitution=new InvolvedInstitution(IssueTypeAssignment.getAssignedInstitution(issue.getType()));
        newInvolvedInstitution.setIssue(issue);
        InvolvedInstitution involvedInstitution=involvedInstitutionRepository.save(newInvolvedInstitution);
        ArrayList<InvolvedInstitution> involvedInstitutions=new ArrayList<>();
        involvedInstitutions.add(involvedInstitution);
        issue.setInvolvedInstitutions(involvedInstitutions);
        return IssueBuilder.toDTO(issue);

    }

    public void delete(Long id){
        involvedInstitutionRepository.deleteByIssueId(id);
        issueRepository.deleteById(id);
    }


    public List<IssueDTO> findByInstitution(Long id) {
        List<Issue> allIssues=issueRepository.findAll();
        Institution institution=institutionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Institution not found: " + id));
        return allIssues.stream().filter(issue ->
            issue.getInvolvedInstitutions().stream().anyMatch(involvedInstitution -> involvedInstitution.getInvolvedInstitutionName().equals(institution.getName()))
            ).map(IssueBuilder::toDTO).collect(Collectors.toList());
    }

    public List<IssueDTO> findByInvolvedUser(Long id, String username) {
        List<Issue> allIssues=issueRepository.findAll();
        Institution institution=institutionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Institution not found: " + id));
        return allIssues.stream().filter(issue ->
                issue.getInvolvedInstitutions().stream().anyMatch(involvedInstitution ->
                        involvedInstitution.getInvolvedInstitutionName().equals(institution.getName()) && involvedInstitution.getInvolvedUserName().equals(username))
        ).map(IssueBuilder::toDTO).collect(Collectors.toList());
    }


    public IssueDTO update(IssueDTO issueDTO) {
        Issue issue=issueRepository.findById(issueDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Issue not found: " + issueDTO.getId()));
        issue.setStatus(issueDTO.getStatus());
        return IssueBuilder.toDTO(issueRepository.save(issue));
    }

    public void addMessage(MessageDTO messageDTO){
        Issue issue=issueRepository.findById(messageDTO.getIssueId()).orElseThrow(() -> new EntityNotFoundException("Issue not found: " + messageDTO.getIssueId()));
        Message message= MessageBuilder.toEntity(messageDTO);
        message.setIssue(issue);
        messageRepository.save(message);
    }

    public void sendNotification(MessageDTO messageDTO) {

        String username=findById(messageDTO.getIssueId()).getUsername();
        System.out.println(username);

        if(!username.equals(messageDTO.getSourceName())){
            com.google.firebase.messaging.Message message = com.google.firebase.messaging.Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(messageDTO.getSourceName())
                            .setBody(new Gson().toJson(messageDTO))
                            .build())
                    .setTopic(username)
                    .build();

            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }
        }


    }


}
