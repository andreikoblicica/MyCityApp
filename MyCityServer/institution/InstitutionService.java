package com.example.community.institution;


import com.example.community.issue.Issue;
import com.example.community.issue.IssueRepository;
import com.example.community.user.User;
import com.example.community.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;

    private final InvolvedInstitutionRepository involvedInstitutionRepository;

    private final IssueRepository issueRepository;

    @Autowired
    public InstitutionService(InstitutionRepository institutionRepository, UserRepository userRepository, InvolvedInstitutionRepository involvedInstitutionRepository, IssueRepository issueRepository){
        this.institutionRepository=institutionRepository;
        this.userRepository=userRepository;
        this.involvedInstitutionRepository=involvedInstitutionRepository;
        this.issueRepository=issueRepository;
    }

    public Institution find(Long id) {
        return institutionRepository.findById(id)
                .orElse(null);
    }

    public InstitutionDTO findById(Long id){
        return InstitutionBuilder.toDTO(find(id));
    }

    public List<InstitutionDTO> findAll() {
        List<Institution> institutions = institutionRepository.findAll();
        return institutions.stream()
                .map(InstitutionBuilder::toDTO)
                .collect(Collectors.toList());
    }


    public InstitutionDTO create(InstitutionDTO institutionDTO) {
        Institution institution= InstitutionBuilder.toEntity(institutionDTO);
        Institution saved=institutionRepository.save(institution);
        return InstitutionBuilder.toDTO(saved);

    }

    public InstitutionDTO update(InstitutionDTO institutionDTO) {
        Institution institution = find(institutionDTO.getId());
        institution.setName(institutionDTO.getName());
        institution.setAddress(institutionDTO.getAddress());
        institution.setPhoneNumber(institutionDTO.getPhoneNumber());
        institution.setWebsite(institutionDTO.getWebsite());
        institution.setEmail(institutionDTO.getEmail());
        return InstitutionBuilder.toDTO(institutionRepository.save(institution));
    }


    public void delete(Long id){
        institutionRepository.deleteById(id);
    }

    public InstitutionDTO findByUserId(Long id) {
        User user=userRepository.findById(id).orElse(null);
        assert user != null;
        return InstitutionBuilder.toDTO(user.getInstitution());
    }

    public InvolvedInstitutionDTO updateInvolvedInstitution(InvolvedInstitutionDTO involvedInstitutionDTO) {
        InvolvedInstitution involvedInstitution=involvedInstitutionRepository.findById(involvedInstitutionDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Involved institution not found: " + involvedInstitutionDTO.getId()));
        involvedInstitution.setInvolvedUserName(involvedInstitutionDTO.getUsername());
        involvedInstitution.setStatus(involvedInstitutionDTO.getIssueStatus());
        return InstitutionBuilder.toDTO(involvedInstitutionRepository.save(involvedInstitution));
    }

    @Transactional
    public InvolvedInstitutionDTO reassignIssue(InvolvedInstitutionDTO involvedInstitutionDTO, Long id) {
       involvedInstitutionRepository.deleteByIssueId(id);
       InvolvedInstitution involvedInstitution=InstitutionBuilder.toEntity(involvedInstitutionDTO);
       Issue issue=issueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Issue not found: " + id));
       issue.setStatus("Opened");
       issueRepository.save(issue);
       involvedInstitution.setIssue(issue);
       return InstitutionBuilder.toDTO(involvedInstitutionRepository.save(involvedInstitution));
    }

    public List<InvolvedInstitutionDTO> shareIssue(List<InvolvedInstitutionDTO> involvedInstitutionDTOS, Long id) {
        Issue issue=issueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Issue not found: " + id));
        List<InvolvedInstitutionDTO> institutionDTOS=new ArrayList<>();
        for(InvolvedInstitutionDTO involvedInstitutionDTO: involvedInstitutionDTOS){
            InvolvedInstitution involvedInstitution=InstitutionBuilder.toEntity(involvedInstitutionDTO);
            involvedInstitution.setIssue(issue);
            InvolvedInstitutionDTO institutionDTO=InstitutionBuilder.toDTO(involvedInstitutionRepository.save(involvedInstitution));
            institutionDTOS.add(institutionDTO);
        }
        return institutionDTOS;
    }

}
