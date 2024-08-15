# G6 Bank

G6 Bank é um projeto bancário desenvolvido em Java utilizando o framework Spring Boot. Ele oferece funcionalidades essenciais para o gerenciamento de contas, clientes, transações e investimentos, com suporte a operações bancárias, gestão de clientes e controle de investimentos financeiros.

## Requisitos

- **Java 17**
- **MySQL**
- **IDE STS (Spring Tool Suite)**
- **Ngrok**
- **Postman**

## Primeiros Passos

### 1. Clonar o Repositório

Clone o repositório ou baixe o arquivo ZIP do projeto e descompacte-o no seu workspace da IDE STS:

```bash
git clone https://github.com/seu-usuario/g6-bank.git

## 2. Criar o Banco de Dados

Acesse o MySQL e crie um banco de dados chamado `banco`:

```sql
CREATE DATABASE banco;

## 3. Configurar o Arquivo `application.properties`

No diretório do projeto, abra o arquivo `src/main/resources/application.properties` e adicione o seu username e password de acesso ao MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banco
spring.datasource.username=SEU_USERNAME
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update

## 4. Iniciar a Aplicação

Abra a IDE STS, importe o projeto e inicie a aplicação com a opção **Run As Spring Boot App**.

## 5. Testar a API

Agora que o backend está rodando, você pode testar as funcionalidades utilizando o Swagger ou Postman.

- **Swagger URL:** `http://localhost:8080/swagger-ui.html`
- **Postman:** Importe a coleção para testar os endpoints da API.

## 6. Tornar a Aplicação Visível na Nuvem com Ngrok

Para expor o backend local na nuvem e permitir a comunicação com o frontend, siga os passos abaixo:

1. Baixe o [ngrok](https://ngrok.com/download) e descompacte o arquivo no seu sistema.
2. Abra um terminal no diretório onde o ngrok foi descompactado.
3. Execute o comando para adicionar o token de autenticação:

    ```bash
    ngrok config add-authtoken 2kU16dluxvKGlJSB5dEgSG0XyUw_5nJWQsPJWWHfM8mmGQP8v
    ```

4. Agora, rode o seguinte comando para expor a aplicação local:

    ```bash
    ngrok http --domain=joseeduardo.ngrok.app 8080
    ```

### Tela Esperada

A seguinte tela deverá aparecer:

![ngrok terminal](link-para-screenshot)

## 7. Acessar o Frontend

Com o backend em execução e exposto na nuvem, acesse o site do frontend em FlutterFlow:

- **URL:** [https://g6bank.flutterflow.app/](https://g6bank.flutterflow.app/)

Pronto! Agora você pode começar a usar o G6 Bank.

