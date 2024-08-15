# G6 Bank

G6 Bank é um projeto bancário desenvolvido em Java utilizando o framework Spring Boot. Ele oferece funcionalidades essenciais para o gerenciamento de contas, clientes, transações e serviços, com suporte a operações bancárias.

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
git clone https://github.com/AugustoLBA/Projeto-final-academia-Java-Accenture.git
```
## 2. Criar o Banco de Dados

Acesse o MySQL e crie um banco de dados chamado `banco`:

```sql
CREATE DATABASE banco;
```
## 3. Configurar o Arquivo `application.properties`

No diretório do projeto, abra o arquivo `src/main/resources/application.properties` e adicione o seu username e password de acesso ao MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banco
spring.datasource.username=SEU_USERNAME
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
```
## 4. Iniciar a Aplicação

Abra a IDE STS, importe o projeto e inicie a aplicação com a opção **Run As Spring Boot App**.

## 5. Testar a API

Agora que o backend está rodando, você pode testar as funcionalidades utilizando o Swagger ou Postman.

- **Swagger URL:** `http://localhost:8080/swagger-ui.html`
- **Postman:** Teste os endpoints da API.

É NECESSARIO CRIAR PRIMEIRAMENTE UM CLIENTE PARA DEPOIS CRIAR UMA CONTA PARA ESSE CLIENTE.

## 6. Tornar a Aplicação Visível na Nuvem com Ngrok

Para expor o backend local na nuvem e permitir a comunicação com o frontend, siga os passos abaixo:

1. Baixe o [ngrok](https://ngrok.com/download) e descompacte o arquivo no seu sistema.
2. Ao executar o arquivo descompactado o terminal abrirá automaticamente.
3. Execute o comando para adicionar o token de autenticação:

    ```bash
    ngrok config add-authtoken 2kU16dluxvKGlJSB5dEgSG0XyUw_5nJWQsPJWWHfM8mmGQP8v
    ```

4. Agora, rode o seguinte comando para expor a aplicação local:

    ```bash
    ngrok http --domain=joseeduardo.ngrok.app 8080
    ```

### Tela Esperada

Uma tela com informações desse tipo deverá aparecer:

```bash
Session Status                online                                                      
Account                       dududutragarcia@gmail.com (Plan: Personal)                  
Version                       3.14.0                                                      
Region                        South America (sa)                                          
Latency                       89ms                                                        
Web Interface                 http://127.0.0.1:4040                                       
Forwarding                    https://joseeduardo.ngrok.app -> http://localhost:8080 
```


## 7. Acessar o Frontend

Com o backend em execução e exposto na nuvem, acesse o site do frontend em FlutterFlow:

- **URL:** [https://g6bank.flutterflow.app/](https://g6bank.flutterflow.app/)

Pronto! Agora você pode começar a usar o G6 Bank.

