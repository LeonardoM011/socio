package com.leonardom011.socio.events.service;

import com.leonardom011.socio.jwt.service.JwtService;
import com.leonardom011.socio.aws.config.S3Buckets;
import com.leonardom011.socio.aws.service.S3Service;
import com.leonardom011.socio.events.controller.dto.EventAddRequest;
import com.leonardom011.socio.events.controller.dto.EventDTO;
import com.leonardom011.socio.events.controller.dto.EventPutRequest;
import com.leonardom011.socio.events.entity.Event;
import com.leonardom011.socio.events.repository.EventRepository;
import com.leonardom011.socio.users.entity.User;
import com.leonardom011.socio.users.repository.UserRepository;
import com.leonardom011.socio.exception.EventNotFoundException;
import com.leonardom011.socio.exception.InvalidAddEventException;
import com.leonardom011.socio.exception.InvalidDeleteEventException;
import com.leonardom011.socio.exception.UserNotOwnerException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventsService {

    private final EventRepository eventRepository;
    private final JwtService jwtService;
    private final Clock clock;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final UserRepository userRepository;

    public Page<EventDTO> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable).map(EventDTO::new);
    }

    public Page<EventDTO> getAllEventsForUser(Pageable pageable, UUID userUUID) throws DataAccessException {
        return eventRepository.findAllByCreatedBy(pageable, userUUID).map(EventDTO::new);
    }

    public EventDTO getEvents(Long eventId) throws EventNotFoundException {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        return new EventDTO(event);
    }

    public Long addEvent(EventAddRequest eventAddRequest) throws Exception {
        User user = jwtService.getCurrentUser();
        Event event = eventAddRequest.toEvent(user, LocalDateTime.now(clock));
        try {
            Event e = eventRepository.save(event);
            return e.getId();
        } catch(DataAccessException e) {
            e.printStackTrace();
            throw new InvalidAddEventException();
        }
    }

    public void deleteEvent(Long id) throws InvalidDeleteEventException {
        try {
            eventRepository.deleteById(id);
        } catch(DataAccessException e) {
            e.printStackTrace();
            throw new InvalidDeleteEventException(id);
        }
    }

    public void updateEvent(Long eventId, EventPutRequest request) throws Exception {
        Event targetEvent = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException(eventId));
        User currentUser = jwtService.getCurrentUser();
        if (!Objects.equals(targetEvent.getCreatedBy().getId(), currentUser.getId())) {
            throw new UserNotOwnerException();
        }
        eventRepository.save(request.toEventFill(targetEvent, eventId, LocalDateTime.now(clock)));
    }

    @Transactional
    public void uploadImage(Long eventId, MultipartFile file) {
        checkIfEventExists(eventId);
        String eventImageId = UUID.randomUUID().toString();
        try {
            s3Service.putObject(
                    s3Buckets.getImages(),
                    "event-images/%s/%s".formatted(eventId, eventImageId),
                    file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        eventRepository.updateEventImageId(eventImageId, eventId);
    }

    public byte[] getImage(Long eventId) throws EventNotFoundException {

        EventDTO event = eventRepository
                .findById(eventId)
                .map(EventDTO::new)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (event.imageId().isBlank()) {
            throw new ResourceNotFoundException("Event with id [%d] image not found".formatted(eventId));
        }

        byte[] image = s3Service.getObject(
                s3Buckets.getImages(),
                "event-images/%s/%s".formatted(eventId, event.imageId())
        );
        return image;
    }

    private void checkIfEventExists(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException(
                    "Event with id [%d] not found".formatted(eventId)
            );
        }
    }
}
