package com.example.community.issue;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.community.UrlMapping.*;

@RestController
public class IssueController {
    private final IssueService issueService;

    public IssueController(IssueService issueService){
        this.issueService=issueService;
    }

    @CrossOrigin
    @GetMapping(ISSUE)
    public ResponseEntity<List<IssueDTO>> findAll() {
        List<IssueDTO> issueDTOS=issueService.findAll();
        return new ResponseEntity<>(issueDTOS, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(INSTITUTION_ISSUE)
    public ResponseEntity<IssueDTO> findById(@PathVariable Long id) {
        IssueDTO issueDTO=issueService.findById(id);
        return new ResponseEntity<>(issueDTO, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(INSTITUTION_ISSUES)
    public ResponseEntity<List<IssueDTO>> findByInstitution(@PathVariable Long id) {
        List<IssueDTO> issueDTOS=issueService.findByInstitution(id);
        return new ResponseEntity<>(issueDTOS, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(INVOLVED_USER)
    public ResponseEntity<List<IssueDTO>> findByInvolvedUser(@PathVariable Long id, @PathVariable String username) {
        List<IssueDTO> issueDTOS=issueService.findByInvolvedUser(id, username);
        return new ResponseEntity<>(issueDTOS, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(USER_ISSUES)
    public ResponseEntity<List<IssueDTO>> findByUsername(@PathVariable String username) {
        List<IssueDTO> issueDTOS=issueService.findByUserUsername(username);
        return new ResponseEntity<>(issueDTOS, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(ISSUE)
    public ResponseEntity<IssueDTO> create(@RequestBody ReportedIssueDTO issueDTO) {
        IssueDTO issue = issueService.create(issueDTO);
        return new ResponseEntity<>(issue,HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(UPDATE_ISSUE_STATUS)
    public ResponseEntity<IssueDTO> update(@Valid @RequestBody IssueDTO issueDTO) {
        IssueDTO issue = issueService.update(issueDTO);
        return new ResponseEntity<>(issue, HttpStatus.OK);
    }



    @CrossOrigin
    @DeleteMapping(ISSUE+ID)
    public void delete(@PathVariable Long id) {
        issueService.delete(id);
    }


    @CrossOrigin
    @PostMapping(ISSUE+"/test")
    public ResponseEntity<IssueDTO> test(@RequestBody IssueDTO issueDTO) {
        System.out.println("here");
        return new ResponseEntity<>(issueDTO, HttpStatus.OK);
    }

}
