package aeroport.bf.controller;

import aeroport.bf.dto.NotificationDto;
import aeroport.bf.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tags(@Tag(name = "Notifications", description = "Gestion des notifications"))
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * POST  /notifications  : Creates a new notification.
     *
     * @param dto {@link NotificationDto}
     * @return {@link NotificationDto}
     */
    @PostMapping("/notifications")
    @Operation(summary = "Creating a new notification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<NotificationDto> create(@Valid @RequestBody final NotificationDto dto) {
        
        return ResponseEntity.ok(notificationService.create(dto));
    }

    /**
     * PUT  /notifications/:id  : Updates an existing notification.
     *
     * @param dto
     * @param id
     * @return {@link NotificationDto}
     */
    @PutMapping("/notifications/{id}")
    @Operation(summary = "Update an existing notification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<NotificationDto> update(@Valid @RequestBody final NotificationDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(notificationService.update(dto, id));
    }

    /**
     * GET / : get all notifications.
     *
     * @return {@link List<NotificationDto>}
     */
    @GetMapping("/notifications")
    @Operation(summary = "Fetch all notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<NotificationDto>> getAll() {
        return new ResponseEntity<>(notificationService.findAll(), HttpStatus.OK);
    }

    /**
     * GET /:id : get notifications.
     *
     * @param id
     * @return {@link List<NotificationDto>}
     */
    @GetMapping("/notifications/{id}")
    @Operation(summary = "Get notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<NotificationDto> findOne(@PathVariable final Long id) {
        return ResponseEntity.ok(notificationService.findOne(id));
    }

    /**
     * DELETE /:id : delete notification.
     *
     * @param id
     * @return {@link List<NotificationDto>}
     */
    @DeleteMapping("/notifications/{id}")
    @Operation(summary = "Remove notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
