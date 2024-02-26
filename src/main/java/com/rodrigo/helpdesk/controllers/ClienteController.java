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

import com.rodrigo.helpdesk.dtos.ClienteRequestDTO;
import com.rodrigo.helpdesk.dtos.ClienteResponseDTO;
import com.rodrigo.helpdesk.model.Cliente;
import com.rodrigo.helpdesk.services.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> create(@Valid @RequestBody ClienteRequestDTO objRequestDTO) {
        Cliente newObj = clienteService.create(objRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId())
                .toUri();
        return ResponseEntity.created(location).body(new ClienteResponseDTO(newObj));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_CLIENTE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable(value = "id") Long id) {
        Cliente obj = clienteService.findById(id);
        return ResponseEntity.ok().body(new ClienteResponseDTO(obj));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_TECNICO')")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> findAll() {
        List<Cliente> list = clienteService.findAll();
        List<ClienteResponseDTO> listResponseDTO = list.stream().map(obj -> new ClienteResponseDTO(obj))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listResponseDTO);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_CLIENTE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_CLIENTE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ClienteResponseDTO> update(@PathVariable(value = "id") Long id,
            @Valid @RequestBody ClienteRequestDTO objRequestDTO) {
        Cliente obj = clienteService.update(id, objRequestDTO);
        return ResponseEntity.ok().body(new ClienteResponseDTO(obj));
    }

}