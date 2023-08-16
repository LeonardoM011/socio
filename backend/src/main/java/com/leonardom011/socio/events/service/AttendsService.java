package com.leonardom011.socio.events.service;

import com.leonardom011.socio.jwt.service.JwtService;
import com.leonardom011.socio.events.controller.dto.EventDTO;
import com.leonardom011.socio.events.entity.Event;
import com.leonardom011.socio.events.entity.Going;
import com.leonardom011.socio.events.repository.EventRepository;
import com.leonardom011.socio.exception.EventAlreadyGoingToException;
import com.leonardom011.socio.exception.EventNotFoundException;
import com.leonardom011.socio.exception.EventNotGoingToException;
import com.leonardom011.socio.exception.UserNotFoundException;
import com.leonardom011.socio.users.controller.dto.UserDTO;
import com.leonardom011.socio.users.entity.User;
import com.leonardom011.socio.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendsService {

    private final EventRepository eventRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public Page<EventDTO> getAllEventsGoingForUser(Pageable pageable, UUID userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return eventRepository
                .findAllEventsGoingToForUser(pageable, userId).map(EventDTO::new);
    }

    public Page<UserDTO> getAllUsersGoingToEvent(Pageable pageable, Long eventId) throws EventNotFoundException {
        // TODO: create query using pageable
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        return new PageImpl<>(event.getGoings().stream().map(Going::getUser).map(UserDTO::new).toList());
    }

    public void goingToEvent(Long eventId) throws EventNotFoundException, UserNotFoundException, EventAlreadyGoingToException {
        User currentUser = jwtService.getCurrentUser();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        Set<Event> events = eventRepository
                .findAllEventsGoingToForUser(Pageable.unpaged(), currentUser.getId()).toSet();
        if (events.contains(event)) {
            throw new EventAlreadyGoingToException(eventId);
        }
        event.getGoings().add(new Going(currentUser, event));
        eventRepository.save(event);
    }

    public void removeGoingToEvent(Long eventId) throws EventNotFoundException, EventNotGoingToException {
        User currentUser = jwtService.getCurrentUser();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));

        Set<Event> events = eventRepository
                .findAllEventsGoingToForUser(Pageable.unpaged(), currentUser.getId()).toSet();
        if (!events.contains(event)) {
            throw new EventNotGoingToException(eventId);
        }

        event.getGoings().remove(new Going(currentUser, event));
        eventRepository.save(event);
    }

}
