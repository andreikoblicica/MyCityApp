package com.example.community.event;


import com.example.community.facility.FacilityDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.community.UrlMapping.*;
import static com.example.community.UrlMapping.ID;

@RestController
@RequestMapping(EVENT)
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService){
        this.eventService=eventService;
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<EventDTO>> findAllEvents() {
        List<EventDTO> eventDTOS=eventService.findAll();
        return new ResponseEntity<>(eventDTOS, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(EVENT_TYPES)
    public ResponseEntity<List<String>> findAllEventTypes() {
        List<String> eventTypes=eventService.findAllEventTypes();
        return new ResponseEntity<>(eventTypes, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(FILTER_STATUS)
    public ResponseEntity<List<EventDTO>> findAllByStatus(@PathVariable String status) {
        List<EventDTO> events=eventService.findByStatus(status);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(USER_EVENTS+ID)
    public ResponseEntity<List<EventDTO>> findByUserId(@PathVariable Long id) {
        List<EventDTO> eventDTOS=eventService.findByUserId(id);
        return new ResponseEntity<>(eventDTOS, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(FAVORITE_EVENTS+ID)
    public ResponseEntity<List<EventDTO>> findFavoritesByUserId(@PathVariable Long id) {
        List<EventDTO> eventDTOS=eventService.findFavoritesByUserId(id);
        return new ResponseEntity<>(eventDTOS, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(EVENT_STATUS)
    public ResponseEntity<List<EventDTO>> findByStatus(@PathVariable String eventStatus) {
        List<EventDTO> eventDTOS=eventService.findByStatus(eventStatus);
        return new ResponseEntity<>(eventDTOS, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<EventDTO> create(@Valid @RequestBody EventDTO eventDTO) {
        EventDTO event = eventService.create(eventDTO);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(UPDATE_EVENT_STATUS)
    public ResponseEntity<EventDTO> updateStatus(@Valid @RequestBody EventDTO eventDTO) {
        EventDTO event = eventService.updateStatus(eventDTO);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping
    public ResponseEntity<EventDTO> update(@Valid @RequestBody EventDTO eventDTO) {
        EventDTO event = eventService.update(eventDTO);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }


    @CrossOrigin
    @DeleteMapping(ID)
    public void delete(@PathVariable Long id) {
        eventService.delete(id);
    }


    @CrossOrigin
    @PostMapping(ADD_TO_FAVORITES)
    public ResponseEntity<Void> addToFavorites(@PathVariable Long userId, @PathVariable Long eventId){
        eventService.addToFavorites(userId, eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(REMOVE_FROM_FAVORITES)
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long userId, @PathVariable Long eventId){
        eventService.removeFromFavorites(userId, eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(IS_FAVORITE)
    public ResponseEntity<Boolean> isFavorite(@PathVariable Long userId, @PathVariable Long eventId){
        return new ResponseEntity<>( eventService.isFavorite(userId, eventId),HttpStatus.OK);
    }


}
