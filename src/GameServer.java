import java.io.*;
import java.net.*;
import java.util.*;


public class GameServer {
    private ServerSocket ss;
    private int numPlayers;
    private int totalPlayers = 0;
    private int numGames = 0;
    Map<Integer, ServerSideConnection> player1 = new HashMap<Integer, ServerSideConnection>();
    Map<Integer, ServerSideConnection> player2 = new HashMap<Integer, ServerSideConnection>();
    //private ServerSideConnection player1;
    //private ServerSideConnection player2;
    private int[][] player1Move = new int[2][2];
    private int[][] player2Move = new int[2][2];

    public GameServer(int port) {
        numPlayers = 0;
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        numPlayers = 0;
    }

    public void acceptConnections() {
        try {
            while (true) {
                Socket s = ss.accept();
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                // Check if the client is human
                int client = 0;
                while (client == 0) {
                    client = dis.readInt();
                }
                // If the client is not human
                if (client == -1 ) {
                    dos.writeInt(totalPlayers);
                    s.close();
                    continue;
                }
                if (numPlayers >= 2) {
                    numGames++;
                    numPlayers -= 2;
                }
                numPlayers++;
                totalPlayers++;
                System.out.println("Player " + numPlayers + " connected");
                ServerSideConnection ssc = new ServerSideConnection(s, numPlayers, numGames);
                if (numPlayers == 1) {
                    player1.put(numGames, ssc); //= ssc;
                } else if (numPlayers == 2) {
                    player2.put(numGames, ssc); //= ssc;
                }
                Thread t = new Thread(ssc);
                t.start();
            }
            //System.out.println("2 players lol");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ServerSideConnection implements Runnable {
        private Socket socket;
        private DataInputStream dis;
        private DataOutputStream dos;
        private int playerID;
        private int gameID;

        public ServerSideConnection(Socket s, int id, int gid) {
            socket = s;
            playerID = id;
            gameID = gid;
            try {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                dos.writeInt(gameID);
                dos.writeInt(playerID);
                if (playerID == 2) {
                    player1.get(gameID).dos.writeInt(1);
                    System.out.println("yes");
                }
                dos.flush();

                while (true) {
                    int id = dis.readInt();
                    if (playerID == 1) {
                        if ((player1Move[0][0] = dis.readInt()) == -1) {
                            totalPlayers--;
                        }
                        player1Move[0][1] = dis.readInt();
                        player1Move[1][0] = dis.readInt();
                        player1Move[1][1] = dis.readInt();
                        System.out.println(id);
                        System.out.println("Player 1 move: " + player1Move[0][0] + ", " + player1Move[0][1] + " : " + player1Move[1][0] + ", " + player1Move[1][1]);	
                        player2.get(gameID).sendMove(id, player1Move);
                        if (player1Move[0][0] == -1) totalPlayers--;
                    } else {
                        if ((player2Move[0][0] = dis.readInt()) == -1) {
                            totalPlayers --;
                        }
                        player2Move[0][1] = dis.readInt();
                        player2Move[1][0] = dis.readInt();
                        player2Move[1][1] = dis.readInt();
                        System.out.println(id);
                        System.out.println("Player 2 move: " + player2Move[0][0] + ", " + player2Move[0][1] + " : " + player2Move[1][0] + ", " + player2Move[1][1]);
                        player1.get(gameID).sendMove(id, player2Move);
                        if (player2Move[0][0] == -1) totalPlayers--;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMove(int id, int[][] move) {
            try {
                dos.writeInt(id);
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
        GameServer gs = new GameServer(420);
        gs.acceptConnections();
    }
}
