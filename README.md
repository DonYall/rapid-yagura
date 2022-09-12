# Rapid Yagura Shogi

Shogi is a Japanese form of what is commonly described as Chess. Both Chess and Shogi share a common ancestor, which was an Indian game known as Chaturanga. 

In Shogi, Yagura is a term used to describe a "castle" or a "fortress", while its direct Japanese translation could also mean something such as "tower" or "keep". I call this Rapid Yagura Shogi because it inherits an idea in Chess called "castling", which slides the king behind a rook in one move. In Shogi, castles are much more complicated and require knowledge of different attacks and traps.

## Releases

The game is nowhere near completion, and I'm still in the testing/debugging phase of the raw game before I move onto implementing more advanced features.
But I'll upload pre-releases so you can check out how the game's progress is doing for your own entertainment, or to help me finish this project.
You can find my releases [here](https://github.com/DonYall/rapid-yagura/releases)
Make sure you have [Java 8](https://www.oracle.com/java/technologies/downloads/) or newer installed on your system before running the Jar file.

Contact me on Discord: DonYall#0567

# How to play

## Setting up the game

Open the jar file found in the [releases](https://github.com/DonYall/rapid-yagura/releases), and choose 'See Servers'. Select an open server and join it. If you are the first person to join that server, you will have to wait for a second player to join. Close the window through a taskbar or task manager to end the game, and it will automatically close your opponent's game.

To play locally, select the option found at the bottom of the window, and then 'Host a game'. Another user on the same network can join the game by typing in the same game code and server ID.

You can move the window around by dragging an empty (unoccupied) space.

### Moving the pieces

Pieces can be moved by dragging and dropping, and each piece moves in its own unique way. You can only move pieces when it's your turn.

### Promotions

Most pieces, including pawns, bishops, and rooks promote upon approaching any of the opponent's last 3 rows. Promotions change how the piece moves and/or activate the piece's special ability.

## Pawns

Pawns move one space forward, but can capture one space diagonally forward. Pawns promote upon reaching the final 3 rows, and gain the ability to move like a silver, as well as their own unique ability.

### Raze
When promoted, Raze's attacks do not immediately capture an enemy piece, but instead attack a sticky bomb onto them. After the oppoent's next turn, all enemy adjacent pieces (not diagonal) to the targetted piece will be killed. The king (Cypher) cannot be killed by Raze's sticky bomb.

### Fade
TBD

### Breach
When promoted, the three enemy pieces adjacent forward to Breach will be stunned for the next turn. Stunned pieces will be unable to move for the duration of their stun. The king (Cypher) cannot be stunned by Breach.

### Reyna
When promoted, Reyna gets the ability to turn invincible on capture. Every capture Reyna makes will make her invincible for the next turn. Once a turn is made without any captures, Reyna is no longer invincible. 

### Jett


### Phoenix


### Skye


### Sage


### KAY/O
