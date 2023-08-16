package com.leonardom011.socio.events.controller;

import com.leonardom011.socio.events.service.InterestsService;
import com.leonardom011.socio.exception.EventAlreadyInterestedToException;
import com.leonardom011.socio.exception.EventNotFoundException;
import com.leonardom011.socio.exception.EventNotInterestedToException;
import com.leonardom011.socio.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class InterestsController {

    private final InterestsService interestedService;
    Logger logger = LoggerFactory.getLogger(InterestsController.class);

    @GetMapping("interests-events/{userId}")
    public ResponseEntity<?> getEventsInterestedForUser(@ParameterObject Pageable pageable, @PathVariable UUID userId) throws UserNotFoundException {
        return ResponseEntity.ok(interestedService.getAllEventsInterestedForUser(pageable, userId));
    }

    @GetMapping("interests-users/{eventId}")
    public ResponseEntity<?> getUsersInterestedToEvent(@ParameterObject Pageable pageable, @PathVariable Long eventId) throws EventNotFoundException {
        return ResponseEntity.ok(interestedService.getAllUsersInterestedToEvent(pageable, eventId));
    }

    @PostMapping("interests/{eventId}")
    public ResponseEntity<?> interestedToEvent(@PathVariable Long eventId) throws EventNotFoundException, EventAlreadyInterestedToException {
        interestedService.interestedToEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("interests/{eventId}")
    public ResponseEntity<?> removeInterestedToEvent(@PathVariable Long eventId)
            throws EventNotFoundException, EventNotInterestedToException {
        interestedService.removeInterestedToEvent(eventId);
        return ResponseEntity.ok().build();
    }

}
