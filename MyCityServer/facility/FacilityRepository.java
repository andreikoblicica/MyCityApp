package com.example.community.facility;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    Optional<Facility> findById(Long id);

    List<Facility> findAll();

    List<Facility> findByType(String type);
}
