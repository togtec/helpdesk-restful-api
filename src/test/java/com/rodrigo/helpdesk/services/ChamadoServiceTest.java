package com.rodrigo.helpdesk.services;

import static com.rodrigo.helpdesk.common.ChamadoConstants.CHAMADO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

import com.rodrigo.helpdesk.dtos.ChamadoDTO;
import com.rodrigo.helpdesk.enums.Prioridade;
import com.rodrigo.helpdesk.enums.Status;
import com.rodrigo.helpdesk.exceptions.ObjectNotFoundException;
import com.rodrigo.helpdesk.model.Chamado;
import com.rodrigo.helpdesk.model.Cliente;
import com.rodrigo.helpdesk.model.Tecnico;
import com.rodrigo.helpdesk.repositories.ChamadoRepository;

@ExtendWith(MockitoExtension.class)
public class ChamadoServiceTest {
  @InjectMocks
  private ChamadoService chamadoService;

  @Mock private ChamadoRepository chamadoRepository;
  @Mock private TecnicoService tecnicoService;
  @Mock private ClienteService clienteService;

    @Test
    public void createChamado_WithValidData_ReturnsChamado() {
      Cliente cliente = new Cliente(1L, "Alessandra Lima", "111.661.890-74", "alelima@gmail.com", "password");
      Tecnico tecnico = new Tecnico(2L, "Rodrigo Tognetta", "550.482.150-95", "tog@mail.com", "password");
      Chamado chamado = new Chamado(null, Prioridade.BAIXA, Status.ABERTO, "Novo chamado", "Observações do novo Chamado", cliente, tecnico);

      when(clienteService.findById(1L)).thenReturn(cliente);
      when(tecnicoService.findById(2L)).thenReturn(tecnico);
      when(chamadoRepository.save(chamado)).thenReturn(chamado);      

      Chamado sut =  chamadoService.create(new ChamadoDTO(chamado));

      assertThat(sut).isEqualTo(chamado);
  }

  @Test
  public void createChamado_withInvalidData_throwsException() {
    Chamado chamadoIvalido = new Chamado(null, null, null, "", "", null, null);
    
    assertThatThrownBy(() -> chamadoService.create(new ChamadoDTO(chamadoIvalido))).isInstanceOf(RuntimeException.class);
  }

  @Test
  public void findChamado_ByExistingId_ReturnsChamado() {
    when(chamadoRepository.findById(1L)).thenReturn(Optional.of(CHAMADO));

    Chamado sut = chamadoService.findById(1L);

    assertThat(sut).isEqualTo(CHAMADO);
  }

  @Test
  public void findChamado_ByNotExistingId_ThrowsException() {
    when(chamadoRepository.findById(99L)).thenThrow(ObjectNotFoundException.class);

    assertThatThrownBy(() -> chamadoService.findById(99L)).isInstanceOf(ObjectNotFoundException.class);
  }

  @Test
  public void findAll_ReturnsAllChamados() {
    List<Chamado> chamados = new ArrayList<>() {
      {
        add(CHAMADO);
      }
    };    
    when(chamadoRepository.findAll()).thenReturn(chamados);

    List<Chamado> sut = chamadoService.findAll();

    assertThat(sut).isNotEmpty();
    assertThat(sut).hasSize(1);
    assertThat(sut.get(0)).isEqualTo(CHAMADO);
  }

  @Test
  public void findAll_ReturnsNoChamado() {
    when(chamadoRepository.findAll()).thenReturn(Collections.emptyList());

    List<Chamado> sut = chamadoService.findAll();

    assertThat(sut).isEmpty();
  }

  @Test
  public void updateChamado_withValidData_RetursChamado() {
    Cliente cliente = new Cliente(1L, "Alessandra Lima", "111.661.890-74", "alelima@gmail.com", "password");
    Tecnico tecnico = new Tecnico(2L, "Rodrigo Tognetta", "550.482.150-95", "tog@mail.com", "password");
    Chamado chamado = new Chamado(3L, Prioridade.BAIXA, Status.ABERTO, "chamado existente", "Observações do Chamado", cliente, tecnico);
    Chamado chamadoModificado = new Chamado(3L, Prioridade.BAIXA, Status.ENCERRADO, "chamado existente", "Chamdo encerrado", cliente, tecnico);

    when(chamadoRepository.findById(3L)).thenReturn(Optional.of(chamado));
    when(chamadoRepository.save(chamadoModificado)).thenReturn(chamadoModificado);

    Chamado sut = chamadoService.update(3L, new ChamadoDTO(chamadoModificado));

    assertThat(sut).isEqualTo(chamadoModificado);
  }

  @Test
  public void updateCliente_withInvalidData_ThrowsException() {
    Chamado chamadoIvalido = new Chamado(3L, null, null, "", "", null, null);

    assertThatThrownBy(() -> chamadoService.update(3L, new ChamadoDTO(chamadoIvalido))).isInstanceOf(RuntimeException.class);
  }

  @Test
  public void updateCliente_withInvalidID_ThrowsException() {
    Cliente cliente = new Cliente(1L, "Alessandra Lima", "111.661.890-74", "alelima@gmail.com", "password");
    Tecnico tecnico = new Tecnico(2L, "Rodrigo Tognetta", "550.482.150-95", "tog@mail.com", "password");
    Chamado chamadoModificado = new Chamado(3L, Prioridade.BAIXA, Status.ENCERRADO, "chamado existente", "Chamdo encerrado", cliente, tecnico);

    when(chamadoRepository.findById(99L)).thenThrow(ObjectNotFoundException.class);

    assertThatThrownBy(() -> chamadoService.update(99L, new ChamadoDTO(chamadoModificado))).isInstanceOf(RuntimeException.class);
  }
  
}
