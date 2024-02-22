package com.example.community.message;


import com.example.community.issue.IssueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.example.community.UrlMapping.SEND;

@RequestMapping
@Controller
public class MessageController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final IssueService issueService;

    public MessageController(IssueService issueService){
        this.issueService=issueService;
    }

    @CrossOrigin
    @PostMapping(SEND)
    public ResponseEntity<Void> sendMessage(@RequestBody MessageDTO messageDTO) {
        issueService.addMessage(messageDTO);
        for(String destination: messageDTO.getDestinationNames()){
            simpMessagingTemplate.convertAndSend("/topic/messages/"+messageDTO.getIssueId()+"/"+destination, messageDTO);
            System.out.println("/topic/messages/"+messageDTO.getIssueId()+"/"+destination);
        }
        issueService.sendNotification(messageDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }





}