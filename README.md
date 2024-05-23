## HelpDesk RESTful API

## Resumo
A HelpDesk RESTful API é um projeto de **transformação** criado para consolidar os conhecimentos adquiridos em **Spring Framework**. A API consiste no **backend** de uma aplicação de **gerenciamento de chamados**. O projeto utiliza **Spring Boot, Spring Web, Spring Data, Spring Validation, Spring Security, Spring Test, JWT e Servidor de Recursos Oauth 2**. A aplicação segue as melhores práticas de desenvolvimento de APIs **RESTful**, retornando sempre **códigos de status HTTP apropriados** para cada tipo de operação.

A aplicação possui controle de **autenticação** e **autorização**, tendo, portanto, áreas que só podem ser acessadas por usuários autenticados de determinado perfil. Todo usuário ao ser cadastrado recebe um perfil, podendo este variar entre **cliente, técnico ou administrador**:

* Usuários com **perfil cliente** podem efetuar seu cadastro no sistema e **consultar** informações sobre **chamados**.
* Usuários com **perfil técnico** podem cadastrar novos **chamados**.
* Usuários com **perfil administrador** podem cadastrar novos **técnicos**.

Além dos **perfis de usuário** a aplicação possui dois **perfis de inicialização**. No perfil **test** a aplicação utiliza um banco de dados **H2**. No perfil **dev** um banco de dados **MySQL**. É possível alterar facilmente o perfil de inicialização da aplicação modificando apenas a propriedade **spring.profiles.active** — do arquivo de configuração **application.properties**.

O projeto possui **testes de unidade** detalhados, além de uma **carga inicial de dados de teste** que permitem facilmente testar no **Postman**, assim que a aplicação é inicializada.

Para maiores informações, visite a página oficial do projeto: <br>
<www.togtec.com.br/projetos/helpdesk-restful-api/resumo.php>

## Tecnologias
  * Java SE (17)
  * Spring Boot 3
  * Maven
  * JPA + Hibernate 
  * H2 (no perfil de inicialização **test**)
  * MySQL (no perfil de inicialização **dev**)
  * JUnit 5 + Mockito (testes)
  
## IDE  
  * Visual Studio Code

## Diagrama de Cenário de Teste
<p align="center">
  <img src="doc/img/img-006-cliente-update.jpg" alt="Diagrama de Cenário de Teste Cliente Update">
</p>

Ver galeria completa de imagens em: <br>
<www.togtec.com.br/projetos/helpdesk-restful-api/imagens.php>

## Tratamento de Exceção
A HelpDesk RESTful API é uma joia do tratamento de exceção, que em caso de erro informa o **código de retorno, o tipo do erro, a descrição do erro e os atributos preenchidos de forma inválida**:
<p align="center">
  <img src="doc/img/tratamento-de-excecao.jpg" alt="Exemplo Tratamento de Exceção">
</p>

## Funcionalidades
- Cliente
    - Efetua cadastro no sistema.
    - Efetua login.
    - Consulta Cliente.
    - Atualiza Cliente.
    - Exclui Cliente.
    - Consulta Chamado.    
- Técnico
    - Efetua login.
    - Consulta Técnico.
    - Consulta lista de Clientes.
    - Cria Chamado.
    - Consulta lista de Chamados.
    - Atualiza Chamado.
- Administrador
    - Efetua login.
    - Cadastra Técnico.
    - Atualiza Técnico.
    - Exclui Técnico.

## Executando o código localmente
Você precisa ter o Java e o Maven instalados e configurados localmente.

Abra a **helpdesk-restful-api** em sua IDE favorita como um **Maven Project** e execute o projeto como **Spring Boot Application**.
