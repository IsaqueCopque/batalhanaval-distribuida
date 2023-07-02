# Projeto Prático 

##### _MATA88 - Fundamentos de Sistemas Distribuídos_
----

## Equipe

Isaque Copque e Matheus Novais

## Proposta do projeto

Nossa proposta de jogo distribuído é a Batalha Naval. Este jogo consiste em tabuleiros com coordenadas de posições onde os jogadores, no mínimo 2, posicionam em seu tabuleiro os seus navios, e por turnos, cada um tenta acertar a posição dos navios oponentes, informando as coordenadas do tabuleiro oponente que desejam atacar.

Existem algumas variações nas formas que a Batalha Naval é jogada. Na versão que escolhemos, cada partida tem apenas 2 jogadores, onde cada um dispõe de 5 navios diferentes que deverão ser posicionados entre as posições de coordenadas A1 e J10. Os tipos de navio são: Porta avião de tamanho 5; Navio-tanque de tamanho 4; Cruzador de tamanho 3; Submarino de tamanho 3 e Destroyer de tamanho 2. Em cada turno, o jogador pode atacar seu oponente em apenas um ataque a uma coordenada. O jogo encerra quando um dos jogadores tem todas as partes de seus navios destruídas pelo oponente,sendo o vencedor aquele que as destruiu.

## Implementação

Implementamos o jogo utilizando uma arquitetura cliente/servidor. Os processos cliente se conectam ao processo servidor e estabelecem uma conexão por meio de um socket TCP. O servidor mantém a partida, lida com as regras do jogo, como turno e acertos de tiros, e tem de atualizar as informações da partida aos clientes enquanto estes realizam suas jogadas. O projeto foi desenvolvido utilizando a linguagem Java.

## Código

### Pacote application

Neste pacote estão as classes que implementam a aplicação e a comunicação entre os processos.

- GameClient:  Representa o processo client. Se comunica com o processo servidor e recebe entradas do jogador.
- GameServer: Representa o processo servidor. Se comunica com o processo cliente e gerencia a partida.

### Pacote game

Neste pacote estão as classes que implementam o jogo.

- Board: Representa o tabuleiro do jogo.
- Position: Representa uma posição no tabuleiro.
- Match: Representa uma partida. Contém a lógica do jogo.
- PrintUtils: Métodos auxiliares para visualização do jogo.

### Pacote game.enums

Neste pacote estão os tipos enumerados que são utilizados no jogo.

- PositionStatus: Representa o estado de uma posição no tabuleiro. Se contém um návio, ou foi atingida, ou ainda não atacada ...
- Ship: Representa os tipos de navios: Porta-avião, Navio-tanque ...

### Pacote game.exception

- GameException: Exceção personalizada de jogo

## Execução

**Requisito**: Java Runtime Enviroment

Faça o download do diretório bin e execute os comandos:

> java application.GameServer [port] - para executar o servidor  
> java application.GameClient [server_ip, port]  - para executar o cliente

Argumentos:

- _port_: porta do servidor, por padrão 5000
- _server_ _ip_: ip do servidor, por padrão localhost
