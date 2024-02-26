package com.rodrigo.helpdesk.services;

import static com.rodrigo.helpdesk.common.TecnicoContants.INVALID_TECNICO;
import static com.rodrigo.helpdesk.common.TecnicoContants.TECNICO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

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

import com.rodrigo.helpdesk.dtos.TecnicoRequestDTO;
import com.rodrigo.helpdesk.exceptions.DataIntegrityViolationException;
import com.rodrigo.helpdesk.exceptions.ObjectNotFoundException;
import com.rodrigo.helpdesk.model.Tecnico;
import com.rodrigo.helpdesk.repositories.PessoaRepository;
import com.rodrigo.helpdesk.repositories.TecnicoRepository;

import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
public class TecnicoServiceTest {
    @InjectMocks
    private TecnicoService tecnicoService;

    @Mock
    private TecnicoRepository tecnicoRepository;
    @Mock
    private PessoaRepository pessoaRepository;
    @Mock
    private BCryptPasswordEncoder encoder;

    @Test
    public void createTecnico_withValidData_returnsTecnico() {
        when(tecnicoRepository.save(TECNICO)).thenReturn(TECNICO);

        Tecnico sut = tecnicoService.create(new TecnicoRequestDTO(TECNICO));

        assertThat(sut).isEqualTo(TECNICO);
    }

    @Test
    public void createTecnico_withInvalidData_throwsException() {
        when(tecnicoRepository.save(INVALID_TECNICO)).thenThrow(ConstraintViolationException.class);

        assertThatThrownBy(() -> tecnicoService.create(new TecnicoRequestDTO(INVALID_TECNICO)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createTecnico_withExistingCpfOrExistingEmail_throwsException() {
        when(tecnicoService.create(new TecnicoRequestDTO(INVALID_TECNICO)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> tecnicoService.create(new TecnicoRequestDTO(INVALID_TECNICO)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findTecnico_byExistingId_returnsTecnico() {
        when(tecnicoRepository.findById(anyLong())).thenReturn(Optional.of(TECNICO));

        Tecnico sut = tecnicoService.findById(1L);

        assertThat(sut).isEqualTo(TECNICO);
    }

    @Test
    public void findTecnico_byNotExistingId_throwsException() {
        when(tecnicoRepository.findById(99L)).thenThrow(ObjectNotFoundException.class);

        assertThatThrownBy(() -> tecnicoService.findById(99L)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    public void findAll_returnsAllTecnicos() {
        List<Tecnico> tecnicos = new ArrayList<>() {
            {
                add(TECNICO);
            }
        };
        when(tecnicoRepository.findAll()).thenReturn(tecnicos);

        List<Tecnico> sut = tecnicoService.findAll();

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.get(0)).isEqualTo(TECNICO);
    }

    @Test
    public void findAll_returnsNoTecnico() {
        when(tecnicoRepository.findAll()).thenReturn(Collections.emptyList());

        List<Tecnico> sut = tecnicoService.findAll();

        assertThat(sut).isEmpty();
    }

    @Test
    public void deleteTecnico_withExistingId_doesNotThrowAnyException() {
        when(tecnicoRepository.findById(anyLong())).thenReturn(Optional.of(TECNICO));

        assertThatCode(() -> tecnicoService.delete(1L)).doesNotThrowAnyException();
    }

    @Test
    public void deleteTecnico_withNotExistingId_throwsException() {
        assertThatThrownBy(() -> tecnicoService.delete(99L)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    public void deleteTecnico_withExistingServiceOrder_throwsException() {
        when(tecnicoRepository.findById(anyLong())).thenReturn(Optional.of(TECNICO));

        doThrow(new DataIntegrityViolationException(
                "Técnico possui ordens de serviço em seu nome e não pode ser excluído!")).when(tecnicoRepository)
                .deleteById(anyLong());

        assertThatThrownBy(() -> tecnicoService.delete(1L)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void updateTecnico_withValidData_RetursTecnico() {
        Tecnico tecnico = new Tecnico(1L, "nome do tecnico", "523.213.260-18", "nome@email.com", "password");
        Tecnico tecnicoModificado = new Tecnico(1L, "tecnico modificado", "523.213.260-18", "nome@email.com",
                "password");
        when(tecnicoRepository.findById(1L)).thenReturn(Optional.of(tecnico));
        when(tecnicoRepository.save(tecnicoModificado)).thenReturn(tecnicoModificado);

        Tecnico sut = tecnicoService.update(1L, new TecnicoRequestDTO(tecnicoModificado));

        assertThat(sut).isEqualTo(tecnicoModificado);
        assertThat(sut).isEqualTo(tecnico); // esse teste falha se tecnicoModificado receber um novo CPF
    }

    @Test
    public void updateTecnico_withInvalidData_ThrowsException() {
        Tecnico tecnico = new Tecnico(1L, "nome do tecnico", "523.213.260-18", "nome@email.com", "password");
        Tecnico tecnicoInvalido = new Tecnico(null, "", "", "", "");
        when(tecnicoRepository.findById(1L)).thenReturn(Optional.of(tecnico));
        when(tecnicoRepository.save(tecnicoInvalido)).thenThrow(ConstraintViolationException.class);

        assertThatThrownBy(() -> tecnicoService.update(1L, new TecnicoRequestDTO(tecnicoInvalido)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void updateTecnico_withCpfOrEmailFromOtherPerson_ThrowsException() {
        Tecnico tecnico = new Tecnico(1L, "nome do tecnico", "523.213.260-18", "nome@email.com", "password");
        Tecnico tecnicoModificado = new Tecnico(1L, "tecnico modificado", "177.409.680-30", "paulo.cintra@globo.com",
                "password");
        when(tecnicoRepository.findById(1L)).thenReturn(Optional.of(tecnico));
        when(tecnicoRepository.save(tecnicoModificado)).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> tecnicoService.update(1L, new TecnicoRequestDTO(tecnicoModificado)))
                .isInstanceOf(RuntimeException.class);
    }

}
