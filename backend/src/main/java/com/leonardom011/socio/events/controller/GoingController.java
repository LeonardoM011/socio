package com.leonardom011.socio.events.controller;

import com.leonardom011.socio.events.service.GoingService;
import com.leonardom011.socio.exception.EventAlreadyGoingToException;
import com.leonardom011.socio.exception.EventNotFoundException;
import com.leonardom011.socio.exception.EventNotGoingToException;
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
@RequestMapping("/api/v1/event/")
@RequiredArgsConstructor
public class GoingController {

    private final GoingService goingService;
    Logger logger = LoggerFactory.getLogger(GoingController.class);

    @GetMapping("all-events-going-for-user/{userId}")
    public ResponseEntity<?> getEventsGoingForUser(@ParameterObject Pageable pageable, @PathVariable UUID userId) throws UserNotFoundException {
        return ResponseEntity.ok(goingService.getAllEventsGoingForUser(pageable, userId));
    }

    @GetMapping("all-users-going-to-event/{eventId}")
    public ResponseEntity<?> getUsersGoingToEvent(@ParameterObject Pageable pageable, @PathVariable Long eventId) throws EventNotFoundException {
        return ResponseEntity.ok(goingService.getAllUsersGoingToEvent(pageable, eventId));
    }

    @PostMapping("going/{eventId}")
    public ResponseEntity<?> gointToEvent(@PathVariable Long eventId) throws EventNotFoundException, UserNotFoundException, EventAlreadyGoingToException {
        goingService.goingToEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("remove-going/{eventId}")
    public ResponseEntity<?> removeGoingToEvent(@PathVariable Long eventId)
            throws EventNotFoundException, EventNotGoingToException {
        goingService.removeGoingToEvent(eventId);
        return ResponseEntity.ok().build();
    }

}
