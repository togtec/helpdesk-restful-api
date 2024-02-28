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
<https://togtec.dev.br/projetos/forum-gamification/resumo.php>

## Tecnologias
  * Java SE (17)
  * Spring Boot 3
  * Maven
  * JPA + Hibernate 
  * H2 (perfil de de inicialização **test**)
  * MySQL (perfil de de inicialização **dev**)
  * JUnit 5 + Mockito (testes)
  
## IDE  
  * Visual Studio Code

## Captura de tela
<p align="center">
  <img src="doc/img/img-004-home-Tatiana-Alcantara.png" alt="Home usuário Tatiana Alcantara">
</p>

Ver galeria completa de imagens em: <br>
<https://togtec.dev.br/projetos/forum-gamification/imagens.php>

## Funcionalidades
1. Visitante acessa o sistema
    - Navega na lista de fóruns
    - Navega na lista de tópicos
    - Navega na lista de comentários
2. Visitante cria uma conta (define login, e-mail, nome e senha)    
3. Visitante efetua login como usuário (cadastra **tópicos** e **comentários**)
    - Ganha 10 pontos por tópico cadastrado
    - Ganha 3 pontos por comentário cadastrado
4. Visitante efetua login como administrador (cadastra **fóruns**, **tópicos** e **comentários**)
    - Ganha 10 pontos por tópico cadastrado
    - Ganha 3 pontos por comentário cadastrado
5. Usuário/administrador acessa o ranking para comparar sua posição em relação aos demais
6. Usuário/administrador edita nome e e-mail
7. Usuário/administrador redefine senha
8. Usuário/administrador efetua logout

## Executando o código localmente
1. Instalar o Servidor de Aplicação JEE **Apache TomCat**
2. Instalar a IDE Eclipse (escolher a oção: **Eclipse IDE for Enterprise Java and Web Developers**)
3. **Integrar** a IDE Eclipse ao Servidor de Aplicação Apache TomCat
4. Fazer o **download** do projeto no repositório git
5. **Importar** o projeto no Eclipse
6. **Atualizar** as dependências Mavem do Projeto
7. **Adicionar** o projeto ao Servidor de Aplicação TomCat
9. Baixar manualmente a dependência **chromedriver.exe** (WebDriver para o navegador Google Chrome — Será utilizada para rodar o teste **E2E** com **Selenium Web Driver**)
    - **obs1:** Escolher a versão do chromedriver compatível com a versão do navagador Google Chrome instalada em sua máquina
    - **obs2:** Armazenar o arquivo chromedriver.exe na pasta: D:\softDev\libraries\WebDriver\bin
10. Instalar o Banco de Dados **PostgreSQL**
11. Instalar o **pgAdmin** (plataforma de administração e gerenciamento para o banco de dados PostgreSQL)
12. Dentro do pgAdmin executar:
    - O script para a criação da **ROLE** ita
    - O script para a criação do **DATABASE** forum_gamification_db
    - O script para a criação das **TABLEs**
    - O script para a criação das **SEQUENCEs**
    - **obs1:** Os scripts se encontram no arquivo **banco_de_dados.sql**, dentro da pasta **sql**, na raiz do repositório
    - **obs2:** Os scripts devem ser executados **um por vez**, na sequência em que aparecem no arquivo   
13. Executar o projeto (escolher a opção **Run on Server**)
14. Abrir uma janela do navegador e digitar a url: **localhost:8080/fg/**

**Muito difícil?**<br>
Nesse caso assista ao vídeo de divulgação do projeto e conheça tudo sem instalar absolutamente nada: <br>
<https://www.youtube.com/watch?v=5M9K9McJ3zg>
