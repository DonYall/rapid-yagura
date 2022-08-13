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
    ArrayList<Piece> pieces;
    boolean canRes = true;

    int x;
    int y;

    public Piece(String agent, int xPos, int yPos, boolean isBlue, ArrayList<Piece> pieces) {
        this.agent = agent;
        this.xPos = xPos;
        this.yPos = yPos;
        this.isBlue = isBlue;
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
        String[] silvers = { "brimstone", "astra", "killjoy", "viper", "uraze", "ufade", "ubreach", "ureyna", "ujett", "uphoenix", "uskye", "usage", "ukayo" };
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


        pieces.add(this);
    }

    public void move(int xPos, int yPos, boolean bypass) {
        if (bypass) {
            if (App.getPiece(xPos * App.pieceSize, yPos * App.pieceSize) != null) {
                if (App.getPiece(xPos *App.pieceSize, yPos * App.pieceSize).isBlue!=isBlue) {
                    
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
            if (squares.stream().anyMatch(a -> Arrays.equals(a, new int[]{xPos, yPos}))) {
                App.lastDeparture[0] = this.xPos;
                App.lastDeparture[1] = this.yPos;
                if (App.getPiece(xPos * App.pieceSize, yPos * App.pieceSize) != null) {
                    if (App.getPiece(xPos *App.pieceSize, yPos * App.pieceSize).isBlue!=isBlue) {
                    Piece p = App.getPiece(xPos *App.pieceSize, yPos * App.pieceSize);
                        if (p.canRes) new DeadPiece(p.agent, p.xPos, p.yPos, p.isBlue, App.deadPieces);
                        App.getPiece(xPos * App.pieceSize, yPos * App.pieceSize).kill();
                        App.selectedPiece = null;
                        try {
                            if (App.blue) App.playSound(App.capturewav);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        x = this.xPos * App.pieceSize;
                        y = this.yPos * App.pieceSize;
                        App.selectedPiece = null;
                        updateMaterial();
                        updateVision();
                        return;
                    }
                } else {
                    try {
                        if (App.blue) App.playSound(App.movewav);
                    } catch (Exception e) {
                        e.printStackTrace();
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
                boolean res = false;
                if (this.agent.equalsIgnoreCase("usage") && App.getDeadPieceFromPos(xPos, yPos, this.isBlue) != null) {
                    DeadPiece d = App.getDeadPieceFromPos(xPos, yPos, this.isBlue);
                    Piece p = new Piece(d.agent, d.xPos, d.yPos, d.isBlue, pieces);
                    p.canRes = false;
                    d.kill();
                    res = true;
                }
                if (!(this.type.equalsIgnoreCase("lance") && Math.abs(this.xPos-xPos) == 0) && !res) {
                    this.xPos = xPos;
                    this.yPos = yPos;
                }
                App.lastDestination[0] = xPos;
                App.lastDestination[1] = yPos;


                if (this.type.equalsIgnoreCase("pawn") && this.isBlue) {
                    if (this.yPos <= 2) {
                        new Piece("u" + this.agent, this.xPos, this.yPos, this.isBlue, pieces);
                        this.kill();
                    }
                } else if (this.type.equalsIgnoreCase("pawn") && !this.isBlue) {
                    if (this.yPos >= 6) {
                        new Piece("u" + this.agent, this.xPos, this.yPos, this.isBlue, pieces);
                        this.kill();
                    }
                }
                
                if (App.blue) App.blue = false;
                App.selectedPiece = null;
                updateMaterial();
                updateVision();
            }
            x = this.xPos * App.pieceSize;
            y = this.yPos * App.pieceSize;
            App.selectedPiece = null;
        }
    }

    public void kill() {
        if (this.type.equalsIgnoreCase("king")) System.out.println("oh no");
        /*
        int i = pieces.indexOf(this);
        pieces.set(i, pieces.get(pieces.size()-1));
        pieces.remove(pieces.size()-1);*/
        pieces.remove(this);
        // new DeadPiece(this.agent, this.xPos, this.yPos, this.isBlue, App.deadPieces);
    }

    public String getType() {
        return(type);
    }

    public void updateXY() {
        this.x = this.xPos*App.pieceSize;
        this.y = this.yPos*App.pieceSize;
    }

    public void updateMaterial() {
        App.blueMaterial = 0;
        App.redMaterial = 0;
        for (Piece p : pieces) {
            if (App.inBounds(p.xPos, p.yPos)) {
                if (p.isBlue) App.blueMaterial += p.value;
                else App.redMaterial += p.value;    
            }
        }
    }

    public void updateVision() {
        App.blueVision = 0;
        App.redVision = 0;
        for (int i = 0; i < pieces.size(); i++) {
            Piece p = pieces.get(i);
            ArrayList<int[]> moves = new ArrayList<int[]>();
            for (int[] move : App.getAvailableSquares(pieces.get(i))) {
                if(!moves.contains(move)) moves.add(move);
            }
            if (p.isBlue) {
                App.blueVision += moves.size();
            } else {
                App.redVision += moves.size();
            }
        }
        if (App.redCastled) {
            App.redVision += 3;
        } else {
            int b = 0;
            int r = 0;
            for (Piece piece : pieces) {
                if (piece.yPos == 8) {
                    b++;
                } else if (piece.yPos == 0) {
                    r++;
                }
            }
            if (r > 6) App.redVision -= 2;
            if (b > 6) App.blueVision -= 2;
        }
        if (App.blueCastled) App.blueVision += 10;
        if (App.redCastled) App.redVision += 10;
        if (App.canCastleChamber(true) || App.canCastleSova(true)) App.blueVision += 2;
        if (App.canCastleChamber(false) || App.canCastleSova(false)) App.redVision += 2;
    }

}
