package com.example.community.event;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long id;
    private String username;
    private String type;
    private String title;
    private String image;
    private String startDateTime;
    private String endDateTime;
    private String description;
    private String location;
    private String status;
    private Long favoriteCount;

}
