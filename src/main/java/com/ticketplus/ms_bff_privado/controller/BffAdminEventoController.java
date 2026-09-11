package com.ticketplus.ms_bff_privado.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/bff/admin/eventos")
@CrossOrigin(origins = "*")
public class BffAdminEventoController {

    private final RestClient restClient;

    public BffAdminEventoController(@Value("${ms.eventos.url:http://localhost:8081}") String msEventosUrl) {
        this.restClient = RestClient.builder().baseUrl(msEventosUrl).build();
    }

    // GET: Solo requiere estar autenticado (configurado en SecurityConfig)
    @GetMapping
    public ResponseEntity<Object> obtenerTodos() {
        Object eventos = restClient.get()
                .uri("/api/v1/eventos")
                .retrieve()
                .body(Object.class);
        return ResponseEntity.ok(eventos);
    }

    // POST: Exige estrictamente el rol ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> crearEvento(@RequestBody Object evento) {
        Object response = restClient.post()
                .uri("/api/v1/eventos")
                .body(evento)
                .retrieve()
                .body(Object.class);
        return ResponseEntity.ok(response);
    }

    // PUT: Exige estrictamente el rol ADMIN para editar
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> actualizarEvento(@PathVariable Long id, @RequestBody Object evento) {
        Object response = restClient.put()
                .uri("/api/v1/eventos/" + id)
                .body(evento)
                .retrieve()
                .body(Object.class);
        return ResponseEntity.ok(response);
    }

    // DELETE: Exige estrictamente el rol ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarEvento(@PathVariable Long id) {
        restClient.delete()
                .uri("/api/v1/eventos/" + id)
                .retrieve()
                .toBodilessEntity();
        return ResponseEntity.ok().build();
    }
}