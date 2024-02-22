package com.example.community.issue;


import com.example.community.institution.InvolvedInstitutionIssueTypeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


public interface IssueRepository extends JpaRepository<Issue, Long> {

    Optional<Issue> findById(Long id);

    List<Issue> findAll();

    void deleteById(Long id);


    List<Issue> findByUserUsername(String username);

    long countByUserId(Long id);

    @Query("SELECT new com.example.community.issue.IssueTypeCount(i.type, COUNT(i)) FROM Issue i GROUP BY i.type")
    List<IssueTypeCount> countByIssueType();

    @Query("SELECT new com.example.community.issue.IssueStatusCount(i.status, COUNT(i)) FROM Issue i GROUP BY i.status")
    List<IssueStatusCount> countByIssueStatus();

    @Query("SELECT new com.example.community.institution.InvolvedInstitutionIssueTypeCount(i.type, COUNT(i))" +
            "FROM Issue i " +
            "JOIN i.involvedInstitutions ii ON i.id = ii.issue.id " +
            "WHERE ii.involvedInstitutionName = ?1 " +
            "GROUP BY i.type")
    List<InvolvedInstitutionIssueTypeCount> countByIssueTypeAndInstitution(String institutionName);

}
