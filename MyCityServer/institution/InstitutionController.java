package com.example.community.institution;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.community.UrlMapping.*;

@CrossOrigin
@RestController
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService){
        this.institutionService=institutionService;
    }

    @GetMapping(INSTITUTION)
    public ResponseEntity<List<InstitutionDTO>> findAll() {
        List<InstitutionDTO> institutionDTOS=institutionService.findAll();
        return new ResponseEntity<>(institutionDTOS, HttpStatus.OK);
    }

    @GetMapping(INSTITUTION+ID)
    public ResponseEntity<InstitutionDTO> findById(@PathVariable Long id) {
        InstitutionDTO institutionDTO=institutionService.findById(id);
        return new ResponseEntity<>(institutionDTO, HttpStatus.OK);
    }

    @GetMapping(USER_INSTITUTION)
    public ResponseEntity<InstitutionDTO> findByUserId(@PathVariable Long id) {
        InstitutionDTO institutionDTO=institutionService.findByUserId(id);
        return new ResponseEntity<>(institutionDTO, HttpStatus.OK);
    }



    @PostMapping(INSTITUTION)
    public ResponseEntity<InstitutionDTO> create(@Valid @RequestBody InstitutionDTO institutionDTO) {
        InstitutionDTO institution = institutionService.create(institutionDTO);
        return new ResponseEntity<>(institution, HttpStatus.OK);
    }

    @PutMapping(INSTITUTION)
    public ResponseEntity<InstitutionDTO> update(@Valid @RequestBody InstitutionDTO institutionDTO) {
        InstitutionDTO institution = institutionService.update(institutionDTO);
        return new ResponseEntity<>(institution, HttpStatus.OK);
    }

    @DeleteMapping(INSTITUTION+ID)
    public void delete(@PathVariable Long id) {
        institutionService.delete(id);
    }


    @PostMapping(UPDATE_ISSUE)
    public ResponseEntity<InvolvedInstitutionDTO> updateInvolvedInstitution(@Valid @RequestBody InvolvedInstitutionDTO involvedInstitutionDTO) {
        InvolvedInstitutionDTO institutionDTO=institutionService.updateInvolvedInstitution(involvedInstitutionDTO);
        return new ResponseEntity<>(institutionDTO, HttpStatus.OK);
    }

    @PostMapping(REASSIGN_ISSUE)
    public ResponseEntity<InvolvedInstitutionDTO> reassignIssue(@Valid @RequestBody InvolvedInstitutionDTO involvedInstitutionDTO, @PathVariable Long id) {
        InvolvedInstitutionDTO institutionDTO=institutionService.reassignIssue(involvedInstitutionDTO, id);
        return new ResponseEntity<>(institutionDTO, HttpStatus.OK);
    }

    @PostMapping(SHARE_ISSUE)
    public ResponseEntity<List<InvolvedInstitutionDTO>> shareIssue(@Valid @RequestBody List<InvolvedInstitutionDTO> involvedInstitutionDTOS, @PathVariable Long id) {
        List<InvolvedInstitutionDTO> institutionDTOS=institutionService.shareIssue(involvedInstitutionDTOS, id);
        return new ResponseEntity<>(institutionDTOS, HttpStatus.OK);
    }



}
