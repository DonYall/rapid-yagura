import java.util.ArrayList;
import java.util.Arrays;

public class Piece {
    int vision;
    int value;
    String type;
    String agent;
    int xPos;
    int yPos;
    boolean isBlue;
    boolean isUlted;
    ArrayList<Piece> pieces;

    int x;
    int y;

    public Piece(String agent, int xPos, int yPos, boolean isBlue, boolean isUlted, ArrayList<Piece> pieces) {
        this.agent = agent;
        this.xPos = xPos;
        this.yPos = yPos;
        this.isBlue = isBlue;
        this.isUlted = isUlted;
        this.pieces = pieces;

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

        if(isUlted) value += 2;

        pieces.add(this);
    }

    public void move(int xPos, int yPos, boolean bypass) {
        if (bypass) {
            if (App.getPiece(xPos * App.pieceSize, yPos * App.pieceSize) != null) {
                if (App.getPiece(xPos *App.pieceSize, yPos * App.pieceSize).isBlue!=isBlue) {
                    // App.getPiece(xPos * App.pieceSize, yPos * App.pieceSize).kill();   
                } else {
                    x = this.xPos * App.pieceSize;
                    y = this.yPos * App.pieceSize;
                    return;
                }
            }
            this.xPos = xPos;
            this.yPos = yPos;

        } else {
            ArrayList<int[]> squares = App.getAvailableSquares(this);
            for (int i = 0; i < squares.size(); i++) {
                for (int j = 0; j < squares.get(i).length; j++) {
                    System.out.print(squares.get(i)[j]);
                }
                System.out.println();
            }
            System.out.println();
            if (squares.stream().anyMatch(a -> Arrays.equals(a, new int[]{xPos, yPos}))) {
                App.lastDeparture[0] = this.xPos;
                App.lastDeparture[1] = this.yPos;
                if (App.getPiece(xPos * App.pieceSize, yPos * App.pieceSize) != null) {
                    if (App.getPiece(xPos *App.pieceSize, yPos * App.pieceSize).isBlue!=isBlue) {
                        if (this.isBlue) {
                            App.blueMaterial += App.getPiece(xPos *App.pieceSize, yPos * App.pieceSize).value;
                        } else {
                            App.redMaterial += App.getPiece(xPos *App.pieceSize, yPos * App.pieceSize).value;
                        }
                        
                        App.getPiece(xPos * App.pieceSize, yPos * App.pieceSize).kill(); 
                        App.selectedPiece = null;
                    } else {
                        x = this.xPos * App.pieceSize;
                        y = this.yPos * App.pieceSize;
                        App.selectedPiece = null;
                        return;
                    }
                }
                if (this.type.equalsIgnoreCase("king")) {
                    if (this.xPos - xPos > 1) {
                        if (this.isBlue) {
                            App.getPiece(0, 8*App.pieceSize).move(xPos+1, yPos, true);
                            App.getPiece((xPos+1)*App.pieceSize, yPos*App.pieceSize).updateXY();
                            App.blueCastled = true;
                        } else {
                            App.getPiece(0, 0).move(xPos+1, yPos, true);
                            App.getPiece((xPos+1)*App.pieceSize, yPos*App.pieceSize).updateXY();
                            App.redCastled = true;
                        }
                        
                    } else if (this.xPos - xPos < -1) {
                        if (this.isBlue) {
                            App.getPiece(8*App.pieceSize, 8*App.pieceSize).move(xPos-1, yPos, true);
                            App.getPiece((xPos-1)*App.pieceSize, yPos*App.pieceSize).updateXY();
                            App.blueCastled = true;
                        } else {
                            App.getPiece(8*App.pieceSize, 0).move(xPos-1, yPos, true);
                            App.getPiece((xPos-1)*App.pieceSize, yPos*App.pieceSize).updateXY();
                            App.redCastled = true;
                        }
                    }
                }
                this.xPos = xPos;
                this.yPos = yPos;
                App.lastDestination[0] = this.xPos;
                App.lastDestination[1] = this.yPos;
                App.blue = !App.blue;
                App.selectedPiece = null;
            }
            x = this.xPos * App.pieceSize;
            y = this.yPos * App.pieceSize;
            App.selectedPiece = null;
        }
    }

    public void kill() {
        pieces.remove(this);
    }

    public String getType() {
        return(type);
    }

    public void updateXY() {
        this.x = this.xPos*App.pieceSize;
        this.y = this.yPos*App.pieceSize;
    }

}
