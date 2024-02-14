package com.rodrigo.helpdesk.services;

import static com.rodrigo.helpdesk.common.ClienteConstants.CLIENTE;
import static com.rodrigo.helpdesk.common.ClienteConstants.INVALID_CLIENTE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rodrigo.helpdesk.dtos.ClienteRequestDTO;
import com.rodrigo.helpdesk.exceptions.DataIntegrityViolationException;
import com.rodrigo.helpdesk.exceptions.ObjectNotFoundException;
import com.rodrigo.helpdesk.model.Cliente;
import com.rodrigo.helpdesk.repositories.ClienteRepository;
import com.rodrigo.helpdesk.repositories.PessoaRepository;

import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
  @InjectMocks
  private ClienteService clienteService;

  @Mock private ClienteRepository clienteRepository;
  @Mock private PessoaRepository pessoaRepository;
  @Mock private BCryptPasswordEncoder encoder;

  @Test
  public void createCliente_WithValidData_ReturnsCliente() {
    when(clienteRepository.save(CLIENTE)).thenReturn(CLIENTE);

    Cliente sut = clienteService.create(new ClienteRequestDTO(CLIENTE));

    assertThat(sut).isEqualTo(CLIENTE);
  }

  @Test
  public void createCliente_WithInvalidData_ThrowsException() {
    when(clienteRepository.save(INVALID_CLIENTE)).thenThrow(ConstraintViolationException.class);

    assertThatThrownBy(() -> clienteService.create(new ClienteRequestDTO(INVALID_CLIENTE))).isInstanceOf(RuntimeException.class);
  }

  @Test
  public void createCliente_WithExistingCpfOrExistingEmail_ThrowsException() {
    when(clienteService.create(new ClienteRequestDTO(INVALID_CLIENTE))).thenThrow(DataIntegrityViolationException.class);

    assertThatThrownBy(() -> clienteService.create(new ClienteRequestDTO(INVALID_CLIENTE))).isInstanceOf(RuntimeException.class);
  }

  @Test
  public void findCliente_ByExistingId_ReturnsCliente() {
    when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(CLIENTE));

    Cliente sut = clienteService.findById(1L);

    assertThat(sut).isEqualTo(CLIENTE);
  }

  @Test
  public void findCliente_ByNotExistingId_ThrowsException() {
    when(clienteRepository.findById(99L)).thenThrow(ObjectNotFoundException.class);

    assertThatThrownBy(() -> clienteService.findById(99L)).isInstanceOf(ObjectNotFoundException.class);
  }

  @Test
  public void findAll_ReturnsAllClientes() {
    List<Cliente> clientes = new ArrayList<>() {
      {
        add(CLIENTE);
      }
    };    
    when(clienteRepository.findAll()).thenReturn(clientes);

    List<Cliente> sut = clienteService.findAll();

    assertThat(sut).isNotEmpty();
    assertThat(sut).hasSize(1);
    assertThat(sut.get(0)).isEqualTo(CLIENTE);
  }

  @Test
  public void findAll_ReturnsNoCliente() {
    when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

    List<Cliente> sut = clienteService.findAll();

    assertThat(sut).isEmpty();    
  }
  
  @Test
  public void deleteCliente_WithExistingId_doesNotThrowAnyException() {
    when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(CLIENTE));

    assertThatCode(() -> clienteService.delete(1L)).doesNotThrowAnyException();
  }

  @Test
  public void deleteCliente_WithNotExistingId_ThrowsException() {
    assertThatThrownBy(() -> clienteService.delete(99L)).isInstanceOf(ObjectNotFoundException.class);
  }

  @Test
  public void deleteCliente_WithExistingServiceOrder_ThrowsException() {
    when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(CLIENTE));
    
    doThrow(new DataIntegrityViolationException("Cliente possui ordens de serviço em seu nome e não pode ser excluído!")).when(clienteRepository).deleteById(anyLong());

    assertThatThrownBy(() -> clienteService.delete(1L)).isInstanceOf(DataIntegrityViolationException.class);
  }
  
  @Test
  public void updateCliente_withValidData_RetursCliente() {
    Cliente cliente = new Cliente(1L, "nome", "523.213.260-18", "nome@email.com", "password");
    Cliente clienteModificado = new Cliente(1L, "modificado", "523.213.260-18", "nome@email.com", "password");    
    when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
    when(clienteRepository.save(clienteModificado)).thenReturn(clienteModificado);

    Cliente sut = clienteService.update(1L, new ClienteRequestDTO(clienteModificado));

    assertThat(sut).isEqualTo(clienteModificado);
    assertThat(sut).isEqualTo(cliente); //esse teste falha se clienteModificado receber um novo CPF
  }

  @Test
  public void updateCliente_withInvalidData_ThrowsException() {
    Cliente cliente = new Cliente(1L, "nome", "523.213.260-18", "nome@email.com", "password");
    Cliente clienteInvalido = new Cliente(null, "", "", "", "");
    when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
    when(clienteRepository.save(clienteInvalido)).thenThrow(ConstraintViolationException.class);

    assertThatThrownBy(() -> clienteService.update(1L, new ClienteRequestDTO(clienteInvalido))).isInstanceOf(RuntimeException.class);
  }

  @Test
  public void updateCliente_withCpfOrEmailFromOtherPerson_ThrowsException() {
    Cliente cliente = new Cliente(1L, "nome", "523.213.260-18", "nome@email.com", "password");
    Cliente clienteModificado = new Cliente(1L, "nome", "177.409.680-30", "paulo.cintra@globo.com", "password");
    when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
    when(clienteRepository.save(clienteModificado)).thenThrow(DataIntegrityViolationException.class);

    assertThatThrownBy(() -> clienteService.update(1L, new ClienteRequestDTO(clienteModificado))).isInstanceOf(RuntimeException.class);
  }

}
