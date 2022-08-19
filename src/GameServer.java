import java.io.*;
import java.net.*;

public class GameServer {
    private ServerSocket ss;
    private int numPlayers;
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    private int[][] player1Move = new int[2][2];
    private int[][] player2Move = new int[2][2];

    public GameServer(int port) {
        numPlayers = 0;
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        numPlayers = 0;
    }

    public void acceptConnections() {
        try {
            while (numPlayers < 2) {
                Socket s = ss.accept();
                numPlayers++;
                System.out.println("Player " + numPlayers + " connected");
                ServerSideConnection ssc = new ServerSideConnection(s, numPlayers);
                if (numPlayers == 1) {
                    player1 = ssc;
                } else {
                    player2 = ssc;
                }
                Thread t = new Thread(ssc);
                t.start();
            }
            System.out.println("2 players lol");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ServerSideConnection implements Runnable {
        private Socket socket;
        private DataInputStream dis;
        private DataOutputStream dos;
        private int playerID;

        public ServerSideConnection(Socket s, int id) {
            socket = s;
            playerID = id;
            try {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                dos.writeInt(playerID);
                dos.flush();

                while (true) {
                    if (playerID == 1) {
                        player1Move[0][0] = dis.readInt();
                        player1Move[0][1] = dis.readInt();
                        player1Move[1][0] = dis.readInt();
                        player1Move[1][1] = dis.readInt();
                        System.out.println("Player 1 move: " + player1Move[0][0] + ", " + player1Move[0][1] + " : " + player1Move[1][0] + ", " + player1Move[1][1]);	
                        player2.sendMove(player1Move);
                    } else {
                        player2Move[0][0] = dis.readInt();
                        player2Move[0][1] = dis.readInt();
                        player2Move[1][0] = dis.readInt();
                        player2Move[1][1] = dis.readInt();
                        System.out.println("Player 2 move: " + player2Move[0][0] + ", " + player2Move[0][1] + " : " + player2Move[1][0] + ", " + player2Move[1][1]);
                        player1.sendMove(player2Move);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMove(int[][] move) {
            try {
                dos.writeInt(move[0][0]);
                dos.writeInt(move[0][1]);
                dos.writeInt(move[1][0]);
                dos.writeInt(move[1][1]);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        GameServer gs = new GameServer(9999);
        gs.acceptConnections();
    }
}
