package com.example.community.news;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.community.UrlMapping.ID;
import static com.example.community.UrlMapping.NEWS;

@RestController
@RequestMapping(NEWS)
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService){
        this.newsService=newsService;
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<NewsDTO>> findAll() {
        List<NewsDTO> newsDTOS=newsService.findAll();
        return new ResponseEntity<>(newsDTOS, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(ID)
    public ResponseEntity<List<NewsDTO>> findByInstitutionId(@PathVariable Long id) {
        List<NewsDTO> newsDTOS=newsService.findByInstitutionId(id);
        return new ResponseEntity<>(newsDTOS, HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping
    public ResponseEntity<NewsDTO> create(@Valid @RequestBody NewsDTO newsDTO) {
        NewsDTO news = newsService.create(newsDTO);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }


    @CrossOrigin
    @DeleteMapping(ID)
    public void delete(@PathVariable Long id) {
        newsService.delete(id);
    }
}
