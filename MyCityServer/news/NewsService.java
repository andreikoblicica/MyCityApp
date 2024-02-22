package com.example.community.news;


import com.example.community.institution.Institution;
import com.example.community.institution.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final InstitutionRepository institutionRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository, InstitutionRepository institutionRepository){
        this.newsRepository=newsRepository;
        this.institutionRepository=institutionRepository;
    }

    public News findById(Long id) {
        return newsRepository.findById(id)
                .orElse(null);
    }

    public List<NewsDTO> findAll() {
        List<News> news = newsRepository.findAll();
        return news.stream()
                .map(NewsBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public List<NewsDTO> findByInstitutionId(Long id) {
        List<News> news = newsRepository.findByInstitutionId(id);
        return news.stream()
                .map(NewsBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public NewsDTO create(NewsDTO newsDTO) {
        News news= NewsBuilder.toEntity(newsDTO);
        Institution institution=institutionRepository.findByName(newsDTO.getInstitution()).orElse(null);
        news.setInstitution(institution);
        News saved=newsRepository.save(news);
        return NewsBuilder.toDTO(saved);
    }

    public void delete(Long id){
        newsRepository.deleteById(id);
    }
}
