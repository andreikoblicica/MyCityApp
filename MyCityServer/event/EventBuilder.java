package com.example.community.event;




import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Locale;


public class EventBuilder {
    public static EventDTO toDTO(Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", Locale.ENGLISH);
        return new EventDTO(
                event.getId(),
                event.getUser().getUsername(),
                EventType.fromString(event.getType()).toString(),
                event.getTitle(),
                event.getImage(),
                event.getStartDateTime().format(formatter),
                event.getEndDateTime() != null ? event.getEndDateTime().format(formatter) : "",
                event.getDescription(),
                event.getLocation(),
                event.getStatus(),
                event.getFavoriteCount());
    }

    public static Event toEntity(EventDTO eventDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return new Event(
                eventDTO.getType(),
                eventDTO.getTitle(),
                eventDTO.getImage(),
                LocalDateTime.parse(eventDTO.getStartDateTime(), formatter),
                !eventDTO.getEndDateTime().equals("") ? LocalDateTime.parse(eventDTO.getEndDateTime(), formatter) : null,
                eventDTO.getDescription(),
                eventDTO.getLocation(),
                eventDTO.getStatus());
    }
}
