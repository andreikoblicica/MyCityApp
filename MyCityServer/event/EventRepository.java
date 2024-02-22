package com.example.community.event;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(Long id);

    List<Event> findAll();

    void deleteById(Long id);

    List<Event> findByUserId(Long id);

    List<Event> findByStatus(String eventStatus);

    @Query("SELECT new com.example.community.event.EventTypeCount(e.type, COUNT(e)) FROM Event e GROUP BY e.type")
    List<EventTypeCount> countByEventType();

    @Query("SELECT new com.example.community.event.EventStatusCount(e.status, COUNT(e)) FROM Event e GROUP BY e.status")
    List<EventStatusCount> countByEventStatus();

    long countByUserId(Long id);

}
