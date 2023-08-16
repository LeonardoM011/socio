package com.leonardom011.socio.events.controller.dto;

import com.leonardom011.socio.events.entity.Event;
import com.leonardom011.socio.events.entity.EventCategory;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.time.LocalDateTime;

@JsonSerialize
public record EventPutRequest(
        @NonNull String name,
        String description,
        @NonNull EventCategory category,
        String city,
        @NonNull String location,
        @NonNull LocalDateTime dateTime,
        @NonNull Boolean isAvailable
) {



    public EventPutRequest(Event event) {
        this(
                event.getName(),
                event.getDescription(),
                event.getCategory(),
                event.getCity(),
                event.getLocation(),
                event.getDateTime(),
                event.getIsAvailable()
        );
    }

    public Event toEventFill(Event event, Long eventId, LocalDateTime localDateTimeNow) {
        return Event.builder()
                .id(eventId)
                .name(this.name)
                .description(this.description)
                .category(this.category)
                .city(this.city)
                .location(this.location)
                .dateTime(this.dateTime)
                .isAvailable(this.isAvailable)
                .createdBy(event.getCreatedBy())
                .createdAt(event.getCreatedAt())
                .lastChange(localDateTimeNow)
                .build();
    }
}
