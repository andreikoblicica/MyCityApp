package com.example.community.institution;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    Optional<Institution> findById(Long id);

    Optional<Institution> findByName(String name);

    List<Institution> findAll();


}
