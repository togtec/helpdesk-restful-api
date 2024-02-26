package com.rodrigo.helpdesk.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rodrigo.helpdesk.dtos.TecnicoRequestDTO;
import com.rodrigo.helpdesk.dtos.TecnicoResponseDTO;
import com.rodrigo.helpdesk.model.Tecnico;
import com.rodrigo.helpdesk.services.TecnicoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/tecnicos")
public class TecnicoController {

    @Autowired
    private TecnicoService tecnicoService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<TecnicoResponseDTO> create(@Valid @RequestBody TecnicoRequestDTO objRequestDTO) {
        Tecnico newObj = tecnicoService.create(objRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId())
                .toUri();
        return ResponseEntity.created(location).body(new TecnicoResponseDTO(newObj));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_TECNICO')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<TecnicoResponseDTO> findById(@PathVariable(value = "id") Long id) {
        Tecnico obj = tecnicoService.findById(id);
        return ResponseEntity.ok().body(new TecnicoResponseDTO(obj));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_TECNICO')")
    @GetMapping
    public ResponseEntity<List<TecnicoResponseDTO>> findAll() {
        List<Tecnico> list = tecnicoService.findAll();
        List<TecnicoResponseDTO> listResponseDTO = list.stream().map(obj -> new TecnicoResponseDTO(obj))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listResponseDTO);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
        tecnicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<TecnicoResponseDTO> update(@PathVariable(value = "id") Long id,
            @Valid @RequestBody TecnicoRequestDTO objRequestDTO) {
        Tecnico obj = tecnicoService.update(id, objRequestDTO);
        return ResponseEntity.ok().body(new TecnicoResponseDTO(obj));
    }

}