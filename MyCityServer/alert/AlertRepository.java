package com.example.community.alert;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    Optional<Alert> findById(Long id);

    List<Alert> findAll();

    List<Alert> findByInstitutionId(Long id);

    long countAlertsByInstitutionId(Long id);
}
