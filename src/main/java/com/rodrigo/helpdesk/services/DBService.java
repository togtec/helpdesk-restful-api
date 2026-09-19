package com.rodrigo.helpdesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigo.helpdesk.dtos.ChamadoDTO;
import com.rodrigo.helpdesk.dtos.ClienteRequestDTO;
import com.rodrigo.helpdesk.dtos.TecnicoRequestDTO;
import com.rodrigo.helpdesk.enums.Perfil;
import com.rodrigo.helpdesk.enums.Prioridade;
import com.rodrigo.helpdesk.enums.Status;
import com.rodrigo.helpdesk.model.Chamado;
import com.rodrigo.helpdesk.model.Cliente;
import com.rodrigo.helpdesk.model.Tecnico;

@Service
public class DBService {

    @Autowired
    private TecnicoService tecnicoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ChamadoService chamadoService;

    public void instanciaDB() {
        TecnicoRequestDTO tec1DTO = new TecnicoRequestDTO(
                new Tecnico(null, "Rodrigo Tognetta", "550.482.150-95", "tog@gmail.com", "1234"));
        tec1DTO.addPerfil(Perfil.ADMIN);
        Tecnico tec1 = tecnicoService.create(tec1DTO);
        Tecnico tec2 = tecnicoService.create(new TecnicoRequestDTO(
                new Tecnico(null, "Priscila Lopez", "903.347.070-56", "prilopez@gmail.com", "1234")));
        Tecnico tec3 = tecnicoService.create(new TecnicoRequestDTO(
                new Tecnico(null, "Alexandre Sanches", "271.068.470-54", "alesanches@gmail.com", "1234")));
        tecnicoService.create(
                new TecnicoRequestDTO(new Tecnico(null, "Mara Cruz", "162.720.120-39", "maracruz@gmail.com", "1234")));
        tecnicoService.create(new TecnicoRequestDTO(
                new Tecnico(null, "Alfredo Costa", "778.556.170-27", "alfcosta@gmail.com", "1234")));

        Cliente cli1 = clienteService.create(new ClienteRequestDTO(
                new Cliente(null, "Alessandra Lima", "111.661.890-74", "alelima@gmail.com", "1234")));
        Cliente cli2 = clienteService.create(new ClienteRequestDTO(
                new Cliente(null, "Flavio Sampaio", "322.429.140-06", "flaviosp@bol.com.br", "1234")));
        Cliente cli3 = clienteService.create(new ClienteRequestDTO(
                new Cliente(null, "Igor Batista", "792.043.830-62", "ibatista@bol.com.br", "1234")));
        clienteService.create(new ClienteRequestDTO(
                new Cliente(null, "Paulo Cintra", "177.409.680-30", "paulo.cintra@globo.com", "1234")));
        clienteService.create(new ClienteRequestDTO(
                new Cliente(null, "Sabrina Coral", "081.399.300-83", "sabcoral@gmail.com", "1234")));

        Chamado cham1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Não consegue conectar à VPN da empresa.",
                "O cliente relatou que não consegue estabelecer conexão com a VPN para acessar os recursos internos da empresa. Durante o atendimento inicial, foi verificado que a conexão com a internet está funcionando normalmente e que outros sites e sistemas externos estão acessíveis. Foi realizada uma nova configuração do cliente VPN, mas o problema permanece. O acesso à VPN continua indisponível e o chamado foi encaminhado para análise do nível 2.",
                cli1, tec1);
        Chamado cham2 = new Chamado(null, Prioridade.ALTA, Status.ABERTO, "Sistema financeiro não está disponível.", 
                "O cliente informou que não consegue acessar o sistema financeiro da empresa desde o início da manhã. Durante o atendimento inicial, foi verificado que o computador possui acesso normal à internet e que outros sistemas estão funcionando normalmente. O problema ocorre somente ao acessar o sistema financeiro, que apresenta uma mensagem de erro após a tentativa de login. O chamado foi registrado para análise do nível 2.",
                cli2, tec1);
        Chamado cham3 = new Chamado(null, Prioridade.BAIXA, Status.ENCERRADO, "Computador apresenta lentidão durante a inicialização.",
                "O cliente relatou que o computador estava levando vários minutos para concluir a inicialização. Durante a análise, foi identificado que diversos programas estavam configurados para iniciar automaticamente com o sistema. Foram desabilitados os programas que não eram necessários e realizada uma limpeza dos arquivos temporários. Após a reinicialização do equipamento, o tempo de inicialização voltou ao normal e o cliente confirmou que o problema foi solucionado.",
                cli3, tec2);
        Chamado cham4 = new Chamado(null, Prioridade.ALTA, Status.ABERTO, "Erro ao acessar o sistema financeiro.",
                "O cliente relatou que consegue utilizar a internet normalmente, porém não consegue acessar o sistema financeiro da empresa. Ao tentar realizar o acesso, é apresentada uma mensagem informando que não foi possível concluir a autenticação. Foi confirmado que o problema ocorre mesmo após uma nova tentativa de acesso. O chamado foi aberto para que o técnico de nível 2 possa verificar a causa do problema.",
                cli3, tec3);
        Chamado cham5 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Impressora não realiza as impressões enviadas.",
                "O cliente relatou que consegue enviar documentos para a impressora, mas os trabalhos permanecem parados na fila de impressão. Durante o atendimento inicial, foi verificado que a impressora está ligada e conectada à rede, e que outros computadores conseguem identificá-la. A fila de impressão foi reiniciada, mas os documentos continuam sem ser processados. O problema permanece em análise pelo nível 2.",
                cli1, tec2);
        Chamado cham6 = new Chamado(null, Prioridade.BAIXA, Status.ENCERRADO, "Mensagens de e-mail não estão sendo recebidas.",
                "O cliente informou que não estava recebendo novas mensagens na caixa de e-mail. Durante a análise, foi verificado que a conta estava funcionando normalmente, porém a caixa de entrada havia atingido o limite de armazenamento disponível. Foram removidas mensagens antigas e desnecessárias, liberando espaço para novos e-mails. Após a liberação de espaço, foi realizado um novo teste e as mensagens passaram a ser recebidas normalmente. O cliente confirmou a solução do problema.",
                cli2, tec1);
        chamadoService.create(new ChamadoDTO(cham1));
        chamadoService.create(new ChamadoDTO(cham2));
        chamadoService.create(new ChamadoDTO(cham3));
        chamadoService.create(new ChamadoDTO(cham4));
        chamadoService.create(new ChamadoDTO(cham5));
        chamadoService.create(new ChamadoDTO(cham6));
    }

}
