# TarefaApi
Api para gestão de tarefas
Documentação Swagger : http://localhost:8080/swagger-ui/index.html

Introdução à API de Tarefas

A API de Tarefas é uma solução projetada para facilitar o gerenciamento de tarefas de forma eficiente e intuitiva. Com recursos de autenticação robustos, permite que usuários se registrem e acessem suas informações de maneira segura. A API oferece um conjunto completo de funcionalidades para criar, listar, atualizar e excluir tarefas, além de possibilitar a atribuição de tarefas a diferentes usuários.

A API de Tarefas permite que apenas criadores atualizem e excluem tarefas.

Os principais objetivos da API incluem:

Gerenciamento de Tarefas: Os usuários podem criar novas tarefas, especificando título, descrição, status e o usuário responsável. Ela também captura o usuario do login que está criando a tarefa e atribui nos campos de criador e emailCriador.

Acesso Seguro: A autenticação é realizada por meio de tokens JWT (JSON Web Token), garantindo que apenas usuários autenticados possam interagir com suas tarefas.

Acompanhamento de Tarefas: Os usuários podem visualizar todas as suas tarefas, filtrando por status para facilitar a gestão de suas responsabilidades.

Atualizações e Exclusões: A API permite que os usuários atualizem detalhes de suas tarefas conforme necessário, além de excluir aquelas que não são mais relevantes, assegurando uma lista de tarefas sempre atualizada e relevante.


Passo a Passo para Usar a API de Tarefas :

1. Configuração Inicial
Antes de começar a usar a API, certifique-se de que o ambiente está configurado corretamente:

Instale o PostgreSQL e crie um banco de dados para a aplicação.
Clone o repositório da API em seu ambiente local.
Configure as credenciais do banco de dados no arquivo de configuração da aplicação (por exemplo no nosso caso, application.properties).

2. Inicializando a API
Compile e inicie a aplicação. Se você estiver usando o Spring Boot, pode fazer isso com o comando:


./mvnw spring-boot:run ou pelo run da IDE 

Verifique se a aplicação está rodando acessando http://localhost:8080 (ou a porta configurada).




3. Registro de um Usuário
Para começar a usar a API, você precisa registrar um usuário. Faça uma requisição POST para o endpoint de registro:

Endpoint: POST /usuarios

Corpo da Requisição (JSON):

![image](https://github.com/user-attachments/assets/3d72b245-12dd-4037-807d-2f7a072e5bb7)
![image](https://github.com/user-attachments/assets/da535ec8-489d-4f83-8ede-0e7ebba51c02)


Resposta Esperada: O usuário será criado e você receberá os detalhes do usuário, menos a senha.


4. Login do Usuário
Após registrar, faça o login para obter um token de autenticação:

Endpoint: POST /usuarios/login

Corpo da Requisição (JSON):

![image](https://github.com/user-attachments/assets/923ca860-955c-44d7-ae81-a64fae0a7f45)
![image](https://github.com/user-attachments/assets/8f8c2104-a2cc-4ffe-bd57-d7cadd3e93d5)


Resposta Esperada: Um token JWT que será usado para autenticar as próximas requisições.




5. Criar uma Tarefa
Com o token JWT, você pode criar uma nova tarefa:

Endpoint: POST /tarefas

Cabeçalho: Adicione o token JWT no cabeçalho de autorização:

Authorization: Bearer seu_token_jwt
Corpo da Requisição (JSON):

![image](https://github.com/user-attachments/assets/fe88fedf-e61d-4f1f-a450-35d4b06726b2)
![image](https://github.com/user-attachments/assets/ad49f245-0bbd-46dd-86ce-69f41d3989b9)
![image](https://github.com/user-attachments/assets/6dc37418-d157-422b-b3ca-2743e3200417)



Resposta Esperada: Detalhes da tarefa criada.



6. Listar Todas as Tarefas
Para visualizar todas as tarefas:

Endpoint: GET /tarefas

Cabeçalho: Adicione o token JWT no cabeçalho de autorização.

Resposta Esperada: Uma lista de todas as tarefas.



7. Obter Tarefa por ID
Para obter detalhes de uma tarefa específica:

Endpoint: GET /tarefas/{id}

Cabeçalho: Adicione o token JWT no cabeçalho de autorização.

Resposta Esperada: Detalhes da tarefa correspondente ao ID fornecido.



8. Atualizar uma Tarefa
Para atualizar uma tarefa existente:

Endpoint: PUT /tarefas/{id}

Cabeçalho: Adicione o token JWT no cabeçalho de autorização.

Corpo da Requisição (JSON):

![image](https://github.com/user-attachments/assets/92070c12-d034-4e32-bd29-c123a8e3e452)
![image](https://github.com/user-attachments/assets/95a0238e-3efd-4952-8f9a-01e38ec496a5)


Resposta Esperada: Detalhes da tarefa atualizada.



9. Excluir uma Tarefa
Para excluir uma tarefa:

Endpoint: DELETE /tarefas/{id}

Cabeçalho: Adicione o token JWT no cabeçalho de autorização.

Resposta Esperada: Confirmação de que a tarefa foi excluída.



10. Listar Tarefas por Status
Para filtrar tarefas com base no status:

Endpoint: GET /tarefas/status/{status}

Cabeçalho: Adicione o token JWT no cabeçalho de autorização.

Resposta Esperada: Lista de tarefas filtradas pelo status especificado. 














