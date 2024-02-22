package com.example.community.news;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class NewsBuilder {
    public static NewsDTO toDTO(News news){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", Locale.ENGLISH);
        return new NewsDTO(news.getId(),
                news.getInstitution()!=null ? news.getInstitution().getName() : "MyCity App",
                news.getTitle(),
                news.getDescription(),
                news.getDateTime().format(formatter),
                news.getImage(),
                news.getWebsiteLink());
    }

    public static News toEntity(NewsDTO newsDTO){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new News(newsDTO.getTitle(),
                newsDTO.getDescription(),
                LocalDateTime.parse(newsDTO.getDateTime(), formatter),
                newsDTO.getImage(),
                newsDTO.getWebsiteLink());
    }
}
