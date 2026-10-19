package com.ticketplus.ms_bff_privado.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/bff/admin/queues")
@CrossOrigin(origins = "*")
public class BffAdminRabbitController {

    private final RestClient restClient;

    public BffAdminRabbitController(@Value("${ms.admin.url:http://localhost:8090}") String msAdminUrl) {
        this.restClient = RestClient.builder().baseUrl(msAdminUrl).build();
    }

    // POST: Exige estrictamente el rol ADMIN para crear colas
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> crearCola(@RequestBody Object queueRequest) {
        Object response = restClient.post()
                .uri("/api/v1/admin/queues")
                .body(queueRequest)
                .retrieve()
                .body(Object.class);
        return ResponseEntity.ok(response);
    }

    // DELETE: Exige estrictamente el rol ADMIN para eliminar colas
    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarCola(@PathVariable String name) {
        restClient.delete()
                .uri("/api/v1/admin/queues/" + name)
                .retrieve()
                .toBodilessEntity();
        return ResponseEntity.ok().build();
    }
}