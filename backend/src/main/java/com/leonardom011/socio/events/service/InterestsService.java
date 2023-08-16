package com.leonardom011.socio.events.service;

import com.leonardom011.socio.jwt.service.JwtService;
import com.leonardom011.socio.events.controller.dto.EventDTO;
import com.leonardom011.socio.events.entity.Event;
import com.leonardom011.socio.events.entity.Interested;
import com.leonardom011.socio.events.repository.EventRepository;
import com.leonardom011.socio.users.controller.dto.UserDTO;
import com.leonardom011.socio.users.entity.User;
import com.leonardom011.socio.users.repository.UserRepository;
import com.leonardom011.socio.exception.EventAlreadyInterestedToException;
import com.leonardom011.socio.exception.EventNotFoundException;
import com.leonardom011.socio.exception.EventNotInterestedToException;
import com.leonardom011.socio.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterestsService {

    private final EventRepository eventRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public Page<EventDTO> getAllEventsInterestedForUser(Pageable pageable, UUID userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return eventRepository
                .findAllEventsInterestedToForUser(pageable, userId).map(EventDTO::new);
    }

    public Page<UserDTO> getAllUsersInterestedToEvent(Pageable pageable, Long eventId) throws EventNotFoundException {
        // TODO: create query using pageable
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        return new PageImpl<>(event.getInterests().stream().map(Interested::getUser).map(UserDTO::new).toList());
    }

    public void interestedToEvent(Long eventId) throws EventNotFoundException, EventAlreadyInterestedToException {
        User currentUser = jwtService.getCurrentUser();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        Set<Event> events = eventRepository
                .findAllEventsInterestedToForUser(Pageable.unpaged(), currentUser.getId()).toSet();
        if (events.contains(event)) {
            throw new EventAlreadyInterestedToException(eventId);
        }
        event.getInterests().add(new Interested(currentUser, event));
        eventRepository.save(event);
    }

    public void removeInterestedToEvent(Long eventId) throws EventNotFoundException, EventNotInterestedToException {
        User currentUser = jwtService.getCurrentUser();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));

        Set<Event> events = eventRepository
                .findAllEventsInterestedToForUser(Pageable.unpaged(), currentUser.getId()).toSet();
        if (!events.contains(event)) {
            throw new EventNotInterestedToException(eventId);
        }

        event.getInterests().remove(new Interested(currentUser, event));
        eventRepository.save(event);
    }

}
