package com.leonardom011.socio.events.controller;

import com.leonardom011.socio.events.controller.dto.EventAddRequest;
import com.leonardom011.socio.events.controller.dto.EventPutRequest;
import com.leonardom011.socio.events.entity.EventCategory;
import com.leonardom011.socio.events.service.EventsService;
import com.leonardom011.socio.exception.EventNotFoundException;
import com.leonardom011.socio.exception.InvalidDeleteEventException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class EventsController {

    private final EventsService eventService;
    Logger logger = LoggerFactory.getLogger(EventsController.class);

    @Operation(
            summary = "Get all events paginated",
            description = "Get all events paginated",
            responses = { @ApiResponse(responseCode = "200", ref = "getAllEventsSuccess") })
    @GetMapping("events")
    public ResponseEntity<?> getAllEvents(
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(eventService.getAllEvents(pageable));
    }

    @Operation(
            summary = "Get event by id",
            description = "Get event by id",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "getEventsSuccess"),
                    @ApiResponse(responseCode = "404", ref = "getEventsEventNotFound") })
    @GetMapping("events/{eventId}")
    public ResponseEntity<?> getEvents(@PathVariable Long eventId) throws EventNotFoundException {
        return ResponseEntity.ok(eventService.getEvents(eventId));
    }

    @Operation(
            summary = "[ORGANIZER] create event",
            description = "Create event (Role_ORGANIZER required), created by will be current logged in user",
            security = @SecurityRequirement(name = "token"),
            responses = {
                    @ApiResponse(responseCode = "201", ref = "addEventsSuccess"),
                    @ApiResponse(responseCode = "403", ref = "addEventsNotLoggedIn"),
                    @ApiResponse(responseCode = "403", ref = "addEventsUserNotOrganizer") })
    @PostMapping(value = "events")
    public ResponseEntity<?> addEvent(
            @Validated
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Event to add",
                    content = @Content(schema = @Schema(implementation = EventAddRequest.class)))
            EventAddRequest request) throws Exception {
        Long eventId = eventService.addEvent(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("api/v1/events/{eventId}")
                .buildAndExpand(Map.of("eventId", eventId))
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(
            summary = "[ORGANIZER] update event",
            description = "update event (authority ORGANIZER required), created by should be current logged in user",
            security = @SecurityRequirement(name = "token"),
            responses = {
                    @ApiResponse(responseCode = "201", ref = "updateEventSuccess"),
                    @ApiResponse(responseCode = "403", ref = "forbidden"),
                    @ApiResponse(responseCode = "403", ref = "updateEventUserNotOwner") })
    @PutMapping("events/{eventId}")
    public ResponseEntity<?> updateEvent(
            @PathVariable Long eventId,
            @Validated
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Event to change",
                    content = @Content(schema = @Schema(implementation = EventPutRequest.class)))
            EventPutRequest request) throws Exception {

        eventService.updateEvent(eventId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("api/v1/events/{eventId}")
                .buildAndExpand(Map.of("id", eventId))
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(
            summary = "[ORGANIZER] delete event",
            description = "delete event (Role_ORGANIZER required), created by should be current logged in user",
            security = @SecurityRequirement(name = "token"),
            responses = {
                    @ApiResponse(responseCode = "204", ref = "deleteEventSuccess"),
                    @ApiResponse(responseCode = "403", ref = "forbidden"),
                    @ApiResponse(responseCode = "403", ref = "deleteEventUserNotOwner") })
    @DeleteMapping("events/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventId) throws InvalidDeleteEventException {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all events created by user paginated",
            description = "Get all events created by user paginated",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "getAllEventsForUserSuccess"),
                    @ApiResponse(responseCode = "404", ref = "getAllEventsForUserUserNotFound") })
    @GetMapping("events-user/{userUUID}")
    public ResponseEntity<?> getAllEventsForUser(@ParameterObject Pageable pageable, @PathVariable UUID userUUID) {
        return ResponseEntity.ok(eventService.getAllEventsForUser(pageable, userUUID));
    }

    @Operation(
            summary = "Get event image",
            description = "Get event image",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "getEventImageSuccess"),
                    @ApiResponse(responseCode = "404", ref = "getEventImageEventNotFound") })
    @GetMapping("events/{eventId}/image")
    public ResponseEntity<byte[]> getEventImage(
            @PathVariable Long eventId) throws EventNotFoundException {
        return ResponseEntity.ok(eventService.getImage(eventId));
    }

    @Operation(
            summary = "Upload event image",
            description = "Upload event image",
            security = @SecurityRequirement(name = "token"),
            responses = {
                    @ApiResponse(responseCode = "201", ref = "uploadEventImageSuccess"),
                    @ApiResponse(responseCode = "404", ref = "uploadEventImageEventNotFound") })
    @PostMapping(
            value = "events/{eventId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadEventImage(
            @PathVariable Long eventId,
            @RequestParam("file") MultipartFile file) {

        eventService.uploadImage(eventId, file);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("api/v1/events/{eventId}/image")
                .buildAndExpand(Map.of("eventId", eventId))
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(
            summary = "Get event categories listed",
            description = "Get all available event categories listed",
            responses = { @ApiResponse(responseCode = "200", ref = "getCategoriesSuccess") })
    @GetMapping("events-categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(EventCategory.values());
    }

}
