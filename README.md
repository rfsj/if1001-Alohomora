# Alohomora

Atualmente, as pessoas enfrentam vários problemas com a disponibilidade de ambientes para alugar. Seja passando muito tempo no telefone ou não encontrando a pessoa responsável pela reserva do ambiente ou até mesmo não ter o horário de funcionamento compatível com o seu. Para isso, nosso app visa automatizar o processo de reserva de ambientes públicos ou privados, eliminando assim, boa parte dos problemas de disponibilidade de ambientes com alguns cliques no próprio smartphone.

## Equipe

* `Eliezer Pedro` 
* `Ricardo Ferreira` 

## DEMO
[DEMO - Alohomora](https://drive.google.com/file/d/1-ALpwbt3FnFxjPnR-2_EsWBZcJNVglPh/view?usp=sharing)


## APK
[APK - Alohomora](https://drive.google.com/file/d/1N7BuD3tFzecvpaxQI2aGU-tT42zW3Mzh/view?usp=sharing )

## Implementação
Inicialmente, a aplicação tem uma tela de login onde pode ser feito o cadastro. Na tela inicial, existe uma botton navigation, com as opções de início, reservar, liberar e logout. 
-  Home
> Foi feita uma recyclerview, onde estão listados, com suas informações, os locais para reservar, além de um filtro para encontrar os locais, sendo esses locais vindos de um Fake Online REST server.
-  Reservar, 
> Existe um formulário com as informações da reserva que será salvo. 
-  Liberar,
> É possível destrancar as sala, contudo, as informações devem corresponder aos dados informados na reserva.
- Logout, 
> É possível deslogar da aplicação.
	Na nuvem, utilizamos a tecnologia NodeRED da IBM e instruções em Javascript para implementar todo o nosso servidor que gerencia os dados de Reserva e salva no banco de dados também hospedado no IBM cloud. Quando o usuário faz uma solicitação de abertura, o servidor checa no banco se as informações estão corretas e envia um comando de liberação ao nodeMCU, se os dados informados na liberação não corresponderem com as informações salvas no banco, o servidor não permite a abertura da tranca eletrônica.
	Na parte física do projeto, utilizamos a IDE do Arduino para compilar todas as instruções em escritas em C++ para que o NodeMCU pudesse enviar e receber informações do nosso servidor hospedado no IBM Cloud.
### ferramentas
- IBM Cloud
	> NodeRED e IBMIoT
- NodeMCU

### Ambientes de desenvolvimento utilizados
- Android Studio 
	> Utilizamos a linguagem Kotlin no Android Studio para construir nosso aplicativo mobile
- Arduino IDE
	> Utilizamos a IDE do arduino para escrever as instruções escritas em C++ no NodeMCU.
-NodeRED
>Utilizamos a tecnologia NodeRED com instruções em javascript para criar o servidor.### APIs
- Firebase
	> Usamos a API do Firebase para implementar a autenticação dos usuários no aplicativo mobile.
- Retrofit 
> Usada para carregar as informações dos ambientes provindas de um Fake Online REST server.

### Principais Classes
- LiberacaoActivity
	> Controla a activity de Liberação da tranca. Pede alguns dados ao usuário, encapsula as informações em um Json e envia para o servidor.
- MainActivity
	> Controla a botton navigation do app e controla a autenticação com o Firebase
- ReservaActivity
> Controla a activity de Reserva do ambiente. Pede alguns dados ao usuário, encapsula as informações em um Json e envia para o servidor.
- SearchActivity
	> Controla a Recycle View e a barra de filtro por título

## Features Futuras
- Tratamento das informações do formulário
> A complexidade em tratar as informações no servidor para autenticação impossibilitou a implantação
- Resposta do servidor ao usuário
> Não conseguimos fazer com o que o servidor enviasse uma mensagem de successful e unsuccessful para aplicação
- Mapa para geolocalização
> Após diversos erros com a criação do mapa (com pins interativos que redirecionava o usuário para reserva), optamos por não colocar, ficando para features futuras
- Caminho mais intuitivo para o usuário para reserva
> Depois do erro anterior, foi optado mudar a rota de reserva, mas com intuito de ser implementada no futuro.

