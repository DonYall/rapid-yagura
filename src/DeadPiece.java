import java.util.ArrayList;
import java.util.Arrays;


public class DeadPiece {
    int value;
    String type;
    String agent;
    int xPos;
    int yPos;
    boolean isBlue;
    ArrayList<DeadPiece> deadPieces;

    int x;
    int y;

    public DeadPiece(String agent, int xPos, int yPos, boolean isBlue, ArrayList<DeadPiece> deadPieces) {
        this.agent = agent;
        this.xPos = xPos;
        this.yPos = yPos;
        this.isBlue = isBlue;
        this.deadPieces = deadPieces;

        x = xPos*App.pieceSize;
        y = yPos*App.pieceSize;

        String[] pawns = { "raze", "fade", "breach", "reyna", "jett", "phoenix", "skye", "sage", "kayo" };
        for (int i = 0; i < pawns.length; i++) {
            if (agent.equalsIgnoreCase(pawns[i])) {
                type = "pawn";
                value = 1;
            }
        }
        if (agent.equalsIgnoreCase("neon")) {
            type = "bishop";
            value = 4;
        }
        if (agent.equalsIgnoreCase("yoru")) {
            type = "rook";
            value = 5;
        }
        if (agent.equalsIgnoreCase("chamber") || agent.equalsIgnoreCase("sova")) {
            type = "lance";
            value = 3;
        }
        if (agent.equalsIgnoreCase("omen")) {
            type = "knight";
            value = 3;
        }
        String[] silvers = { "brimstone", "astra", "killjoy", "viper" };
        for (int i = 0; i < silvers.length; i++) {
            if (agent.equalsIgnoreCase(silvers[i])) {
                type = "silver";
                value = 2;
            }
        }
        if (agent.equalsIgnoreCase("cypher")) {
            type = "king";
            value = 0;
        }


        deadPieces.add(this);
    }

    public void kill() {
        deadPieces.remove(this);
    }

    public String getType() {
        return(type);
    }

    public void updateXY() {
        this.x = this.xPos*App.pieceSize;
        this.y = this.yPos*App.pieceSize;
    }

}
