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
When promoted, the opponent will be blinded (unable to see your pieces) for two turns. This ability cannot be reactivated.

### Breach
When promoted, the three enemy pieces adjacent forward to Breach will be stunned for the next turn. Stunned pieces will be unable to move for the duration of their stun. The king (Cypher) cannot be stunned by Breach. This ability cannot be reactivated

### Reyna
When promoted, Reyna gets the ability to turn invincible on capture. Every capture Reyna makes will make her invincible for the next turn. Once a turn is made without any captures, Reyna is no longer invincible. 

### Jett
When promoted, Jett turns into an attack helicopter. She gets the ability to move and capture anywhere 2 spaces forward, backward, left, or right from her original square.

### Phoenix
When promoted, Phoenix creates a respawn point for himself at his promotion square. Whenever he is captured, he is instead transferred back to his respawn point, and replaces any current piece occupying it. This ability cannot be reactivated.

### Skye
When promoted, Skye stuns 4 random enemy pieces for the next turn. Stunned pieces will be unable to move for the duration of their stun. The king (Cypher) can also be stunned by Skye. This ability cannot be reactivated.

### Sage
When promoted, Sage gets the ability to revive as many dead pieces as she wants, up to a total of 1 time. Once a piece has been revived, it cannot be revived again.

### KAY/O
TBD

## Bishop

Bishops can move as many spaces possible diagonally in any direction. Upon promotion, they gain the ability to move 1 space up, down, left, and right (similar to the Horse in Shogi) as well as keeping their ability to move diagonally like a bishop.

### Neon
Neon is the only bishop in the game. Both players' Neons will be facing each other at the starting position.

## Rook

Rooks can move as many spaces possible up, down, left, or right. Upon promotion, they gain the ability to move 1 space in any direction diagonally (similar to the Dragon in Shogi), as well as keeping their ability to move like a rook.

### Yoru

Yoru is the only rook in this game.

## Lances

Lances can move one space sideways, and promote when castled. They can capture or check any piece which is visible in front of them, no matter how far. Lances cannot capture through other pieces.

### Chamber
TBD

### Sova
TBD

## Silvers

Silvers can move to any adjacent square 1 space away from them, except for directly left, right, or down. Silvers do not promote, as they already spawn with an ability.

### Brimstone
Brimstone can attach himself to another piece, placing a 'Stim Beacon' on it. The Stim Beacon makes the piece move the same way it would normally, added to the way a silver would move. If a Stim Beacon is attached to a silver, it does nothing. Once a Stim Beacon is placed, the Brimstone dies.

### Astra
TBD

### Killjoy
TBD

### Viper
TBD

## Knights

Knights move in an L-shaped pattern. Along with the promoted Jett, it can 'hop over' allied or enemy pieces. Knights promote upon reaching the last three rows.

### Omen
Omen is the only knight in the game, and each team has two Omens. When it is promoted, it gets the ability to move to any unoccupied space on the board. This ability cannot be reactivated.

## King

The king can move in a donut-like pattern. It can move 1 space in any direction, including diagonals. Once the king is unable to be protected, the team loses. The king does not promote. The king can also castle once the spaces between it and an unmoved lance have been cleared out. The king can only castle once.

### Cypher
Cypher is the only king in the game. 
