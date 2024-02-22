package com.example.community.news;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    Optional<News> findById(Long id);

    List<News> findAll();

    List<News> findByInstitutionId(Long id);

    long countNewsByInstitutionId(Long id);
}
