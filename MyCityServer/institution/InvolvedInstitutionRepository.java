package com.example.community.institution;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InvolvedInstitutionRepository extends JpaRepository<InvolvedInstitution, Long> {

    Optional<InvolvedInstitution> findById(Long id);

    void deleteByIssueId(Long id);

    @Query("SELECT new com.example.community.institution.InvolvedInstitutionIssueStatusCount(i.status, COUNT(i)) FROM InvolvedInstitution i WHERE i.involvedInstitutionName = ?1 GROUP BY i.status")
    List<InvolvedInstitutionIssueStatusCount> countByIssueStatus(String institutionName);

    long countByInvolvedInstitutionName(String name);

}