package com.example.community.event;


import com.example.community.facility.Facility;
import com.example.community.facility.FacilityBuilder;
import com.example.community.user.User;
import com.example.community.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Transactional
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository=eventRepository;
        this.userRepository=userRepository;
    }

    public Event find(Long id) {
        return eventRepository.findById(id)
                .orElse(null);
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElse(null);
    }

    public List<EventDTO> findAll() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(EventBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> findByUserId(Long id) {
        List<Event> eventList = eventRepository.findByUserId(id);
        return eventList.stream()
                .map(EventBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> findByStatus(String status) {
        List<Event> eventList = eventRepository.findByStatus(status);
        return eventList.stream()
                .map(EventBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public EventDTO create(EventDTO eventDTO) {
        Event event= EventBuilder.toEntity(eventDTO);
        User user=userRepository.findByUsername(eventDTO.getUsername()).orElse(null);
        event.setUser(user);
        Event saved=eventRepository.save(event);
        return EventBuilder.toDTO(saved);

    }

    public EventDTO update(EventDTO eventDTO) {
        Event event = find(eventDTO.getId());
        event.setTitle(eventDTO.getTitle());
        event.setLocation(eventDTO.getLocation());
        event.setType(eventDTO.getType());
        event.setImage(eventDTO.getImage());
        event.setDescription(event.getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        event.setStartDateTime(LocalDateTime.parse(eventDTO.getStartDateTime(), formatter));
        event.setEndDateTime(!eventDTO.getEndDateTime().equals("") ? LocalDateTime.parse(eventDTO.getEndDateTime(), formatter) : null);
        return EventBuilder.toDTO(eventRepository.save(event));

    }

    public void delete(Long id){
        eventRepository.deleteById(id);
    }

    public List<String> findAllEventTypes() {
        return Arrays.stream(EventType.values()).map(EventType::toString).collect(Collectors.toList());
    }

    public EventDTO updateStatus(EventDTO eventDTO) {
        Event event=find(eventDTO.getId());
        event.setStatus(eventDTO.getStatus());
        return EventBuilder.toDTO(eventRepository.save(event));
    }

    public List<EventDTO> findFavoritesByUserId(Long id) {
        User user=userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        return user.getFavorites().stream()
                .map(EventBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public void addToFavorites(Long userId, Long eventId) {
        User user=userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        Event event=eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
        event.setFavoriteCount(event.getFavoriteCount()+1);
        user.getFavorites().add(event);
    }

    public void removeFromFavorites(Long userId, Long eventId) {
        User user=userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        Event event=eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
        event.setFavoriteCount(event.getFavoriteCount()-1);
        user.getFavorites().remove(event);
    }

    public Boolean isFavorite(Long userId, Long eventId) {
        User user=userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        Event event=eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
        return user.getFavorites().stream().anyMatch(e -> e.getId().equals(event.getId()));
    }


    @Scheduled(fixedRate = 300000)
    public void setFinishedEvents(){
        LocalDateTime currentDateTime=LocalDateTime.now();
        List<Event> events=eventRepository.findAll();
        for(Event event: events){
            if(!event.getStatus().equals("Finished")){
                if(event.getEndDateTime()!=null){
                    if (event.getEndDateTime().plusHours(3).isBefore(currentDateTime)) {
                        event.setStatus("Finished");
                        eventRepository.save(event);
                    }
                }else{
                    if (event.getStartDateTime().plusDays(1).isBefore(currentDateTime)) {
                        event.setStatus("Finished");
                        eventRepository.save(event);
                    }
                }
            }
        }
    }
}
