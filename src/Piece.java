import java.util.ArrayList;
import java.util.Arrays;


public class Piece {
    int vision;
    float value;
    String type;
    String agent;
    int xPos;
    int yPos;
    boolean isBlue;
    ArrayList<Piece> pieces;
    boolean canRes = true;
    boolean isUlted = false;
    boolean isStunned = false;
    boolean isStimmed = false;

    int x;
    int y;

    public Piece(String agent, int xPos, int yPos, boolean isBlue, ArrayList<Piece> pieces) {
        //System.out.println("new " + agent);
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
        if (agent.equalsIgnoreCase("uneon")) {
            type = "bishop";
            this.isUlted = true;
            value = 5;
        }
        if (agent.equalsIgnoreCase("yoru")) {
            type = "rook";
            value = 5;
        }
        if (agent.equalsIgnoreCase("uyoru")) {
            type = "rook";
            this.isUlted = true;
            value = 6;
        }
        if (agent.equalsIgnoreCase("chamber") || agent.equalsIgnoreCase("sova") || agent.equalsIgnoreCase("usova")|| agent.equalsIgnoreCase("uchamber")) {
            type = "lance";
            value = 3;
        }
        if (agent.equalsIgnoreCase("omen") || agent.equalsIgnoreCase("uomen")) {
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

        if (this.canRes) {
            if (this.agent.equalsIgnoreCase("uphoenix")) {
                App.playSound(App.phoenixURL);
                if (this.isBlue) {
                    App.bluePhoenix[0] = this.xPos;
                    App.bluePhoenix[1] = this.yPos;
                } else if (!this.isBlue) {
                    App.redPhoenix[0] = this.xPos;
                    App.redPhoenix[1] = this.yPos;
                }
            }
    
            else if (this.agent.equalsIgnoreCase("uomen")) {
                this.isUlted = true;
            }
    
            else if (this.agent.equalsIgnoreCase("ureyna")) {
                App.playSound(App.reynaURL);
            }
    
            else if (this.agent.equalsIgnoreCase("ubreach")) {
                App.playSound(App.breachURL);
                int mod = -1;
                if (!this.isBlue) mod = 1;
                int[] xMoves = {-1, 0, 1};
                for (int i = 0; i < xMoves.length; i++) {
                    if (App.getPieceFromPos(this.xPos+xMoves[i], yPos+1*mod) != null) {
                        if (App.getPieceFromPos(this.xPos+xMoves[i], yPos+1*mod).isBlue != this.isBlue && !App.getPieceFromPos(this.xPos+xMoves[i], yPos+1*mod).type.equalsIgnoreCase("king")) {
                            App.getPieceFromPos(this.xPos+xMoves[i], yPos+1*mod).isStunned = true;
                            App.stunnedPieces.add(App.getPieceFromPos(this.xPos+xMoves[i], yPos+1*mod));
                        }
                    }
                }
            }

            else if (this.agent.equalsIgnoreCase("uskye")) {
                // play the sound effect
                int i = 0;
                while (i < 4) {
                    Piece p = pieces.get((int) (Math.random() * (pieces.size()-1)));
                    if (p.isBlue != this.isBlue && !p.isStunned && !p.type.equalsIgnoreCase("king")) {
                        p.isStunned = true;
                        App.stunnedPieces.add(p);
                        i++;
                    }
                }
            }
        }

        if (this.isStimmed) this.value += 1.6;
        pieces.add(this);
    }

    public void move(int xPos, int yPos, boolean bypass) {
        if (bypass) {
            if (App.getPiece(xPos * App.pieceSize, yPos * App.pieceSize) != null) {
                if (App.getPiece(xPos *App.pieceSize, yPos * App.pieceSize).isBlue!=isBlue) {
                    
                } else {
                    if (!this.agent.equalsIgnoreCase("brimstone")) {
                        x = this.xPos * App.pieceSize;
                        y = this.yPos * App.pieceSize;
                        return;    
                    }
                }
            }
            
            this.xPos = xPos;
            this.yPos = yPos;

        } else {
            ArrayList<int[]> squares = App.getAvailableSquares(this);
            String prefix;
            if (this.type.equalsIgnoreCase("pawn")) {
                prefix = Character.toString(this.xPos+97);
            } else {
                prefix = this.getPrefix();
            }
            String action = "";
            String suffix = Character.toString(xPos+97) + (9-yPos);
            if (squares.stream().anyMatch(a -> Arrays.equals(a, new int[]{xPos, yPos}))) {
                Piece r = pieces.stream().filter((s) -> (s.isBlue == this.isBlue) && s != null && s.agent.equalsIgnoreCase("ureyna")).findFirst().orElse(null);
                if (r != null) {
                    r.isUlted = false;
                }
                App.lastDeparture[0] = this.xPos;
                App.lastDeparture[1] = this.yPos;
                boolean boom = false;
                if (App.getPiece(xPos * App.pieceSize, yPos * App.pieceSize) != null) {
                    if (App.getPiece(xPos *App.pieceSize, yPos * App.pieceSize).isBlue!=isBlue) {
                        action = "x";
                        Piece p = App.getPiece(xPos *App.pieceSize, yPos * App.pieceSize);
                        if (this.agent.equalsIgnoreCase("uraze")) {
                            boom = true;
                            App.playSound(App.satchelURL);
                            if (p.isBlue) {
                                App.blueBoom = p;
                            } else {
                                App.redBoom = p;
                            }
                        } else {
                            if (p.agent.equalsIgnoreCase("uphoenix") && p.canRes) {
                                if (p.isBlue) {
                                    if (App.inBounds(App.bluePhoenix[0], App.bluePhoenix[1])) {
                                        Piece pp = App.getPiece(App.bluePhoenix[0], App.bluePhoenix[1]);
                                        if (pp != null) {
                                            new DeadPiece(pp.agent, pp.xPos, pp.yPos, pp.isBlue, App.deadPieces);
                                            pp.kill();
                                        }
                                        p.move(App.bluePhoenix[0], App.bluePhoenix[1], true);
                                        p.updateXY();
                                        App.bluePhoenix[0] = 10;
                                        App.bluePhoenix[1] = 10;
                                    } else {
                                        if (p.canRes) new DeadPiece(p.agent, p.xPos, p.yPos, p.isBlue, App.deadPieces);
                                        App.getPiece(xPos * App.pieceSize, yPos * App.pieceSize).kill();
                                        App.selectedPiece = null;
                                        if (App.blue) App.playSound(App.captureURL);
                                    }
                                } else {
                                    if (App.inBounds(App.redPhoenix[0], App.redPhoenix[1])) {
                                        Piece pp = App.getPiece(App.redPhoenix[0], App.redPhoenix[1]);
                                        if (pp != null) {
                                            new DeadPiece(pp.agent, pp.xPos, pp.yPos, pp.isBlue, App.deadPieces);
                                            pp.kill();
                                        }
                                        p.move(App.redPhoenix[0], App.redPhoenix[1], true);
                                        p.updateXY();
                                        App.redPhoenix[0] = 10;
                                        App.redPhoenix[1] = 10;
                                    } else {
                                        if (p.canRes) new DeadPiece(p.agent, p.xPos, p.yPos, p.isBlue, App.deadPieces);
                                        App.getPiece(xPos * App.pieceSize, yPos * App.pieceSize).kill();
                                        App.selectedPiece = null;
                                        if (App.blue) App.playSound(App.captureURL);
                                    }
                                }
                            } else {
                                if (p.canRes) new DeadPiece(p.agent, p.xPos, p.yPos, p.isBlue, App.deadPieces);
                                App.getPiece(xPos * App.pieceSize, yPos * App.pieceSize).kill();
                                App.selectedPiece = null;
                                if (App.blue) App.playSound(App.captureURL);
                                if (this.agent.equalsIgnoreCase("ureyna")) {
                                    App.playSound(App.laughURL[(int)Math.round(Math.random())]);
                                    this.isUlted = true;
                                }

                            }
                        }
                    } else {
                        if (this.agent.equalsIgnoreCase("brimstone")) {
                            action = "w";
                            if (App.blue) {
                                App.lastBlueMove = prefix + action + suffix;
                                App.lastRedMove = "";
                            } else {
                                App.lastRedMove = prefix + action + suffix;
                            }
                            App.getPieceFromPos(xPos, yPos).isStimmed = true;
                            App.getPieceFromPos(xPos, yPos).value += 1.6;
                            this.kill();
                            if (App.pvp) App.blue = !App.blue;
                            App.selectedPiece = null;
                            App.move++;
                            return;
                        } else {
                            x = this.xPos * App.pieceSize;
                            y = this.yPos * App.pieceSize;
                            App.selectedPiece = null;
                            updateMaterial();
                            updateVision();
                            return;
                        }
                    }
                } else {
                    if (App.blue) App.playSound(App.moveURL);
                }
                if (this.type.equalsIgnoreCase("king")) {
                    if (this.xPos - xPos > 1) {
                        action = "c";
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
                        action = "c";
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
                    action = "o";
                    DeadPiece d = App.getDeadPieceFromPos(xPos, yPos, this.isBlue);
                    Piece p = new Piece(d.agent, d.xPos, d.yPos, d.isBlue, pieces);
                    App.playSound(App.sageURL);
                    p.canRes = false;
                    d.kill();
                    res = true;
                }
                if (!(this.type.equalsIgnoreCase("lance") && Math.abs(this.xPos-xPos) == 0) && !res && !boom) {
                    this.xPos = xPos;
                    this.yPos = yPos;
                }
                App.lastDestination[0] = xPos;
                App.lastDestination[1] = yPos;

                if (!App.search) {
                    if (this.isBlue) {
                        if (App.blueBoom != null) {
                            int[] xMoves = {0, 0, 1, -1};
                            int[] yMoves = {-1, 1, 0, 0};
                            for (int index = 0; index < xMoves.length; index++) {
                                Piece p = App.getPieceFromPos(App.blueBoom.xPos+xMoves[index], App.blueBoom.yPos+yMoves[index]);
                                if (p != null) {
                                    if (p.isBlue == this.isBlue && p.type.equalsIgnoreCase("king")) {
                                        if (p.canRes) {
                                            new DeadPiece(p.agent, p.xPos, p.yPos, p.isBlue, App.deadPieces);
                                        }
                                        p.kill();
                                    }
                                }
                            }
                            App.blueBoom.kill();
                            App.blueBoom = null;
                            App.playSound(App.tntURL);
                            App.playSound(App.obliteratedURL);
                        }
                    } else {
                        if (App.redBoom != null) {
                            int[] xMoves = {0, 0, 1, -1};
                            int[] yMoves = {-1, 1, 0, 0};
                            for (int index = 0; index < xMoves.length; index++) {
                                Piece p = App.getPieceFromPos(App.redBoom.xPos+xMoves[index], App.redBoom.yPos+yMoves[index]);
                                if (p != null) {
                                    if (p.isBlue == this.isBlue && !p.type.equalsIgnoreCase("king")) {
                                        if (p.canRes) {
                                            new DeadPiece(p.agent, p.xPos, p.yPos, p.isBlue, App.deadPieces);
                                        }
                                        p.kill();
                                    }
                                }
                            }
                            App.redBoom.kill();
                            App.redBoom = null;
                            App.playSound(App.tntURL);
                            App.playSound(App.obliteratedURL);
                        }
                    }
                }

                if (this.agent.equalsIgnoreCase("uomen") && this.isUlted) {
                    this.isUlted = false;
                    App.playSound(App.scatterURL);
                }

                for (Piece p : App.stunnedPieces) {
                    if (p.isBlue == this.isBlue) {
                        p.isStunned = false;
                    }
                }

                for (int i = 0; i < App.stunnedPieces.size(); i++) {
                    if (!App.stunnedPieces.get(i).isStunned) {
                        App.stunnedPieces.remove(App.stunnedPieces.get(i));
                        i--;
                    }
                }

                if (this.isBlue && !this.agent.startsWith("u") && !this.type.equalsIgnoreCase("king") && !this.type.equalsIgnoreCase("silver")) {
                    if (this.yPos <= 2) {
                        Piece p = new Piece("u" + this.agent, this.xPos, this.yPos, this.isBlue, pieces);
                        p.canRes = this.canRes;
                        p.isStimmed = this.isStimmed;
                        this.kill();
                    }
                } else if (!this.isBlue && !this.agent.startsWith("u") && !this.type.equalsIgnoreCase("king") && !this.type.equalsIgnoreCase("silver")) {
                    if (this.yPos >= 6) {
                        Piece p = new Piece("u" + this.agent, this.xPos, this.yPos, this.isBlue, pieces);
                        p.canRes = this.canRes;
                        p.isStimmed = this.isStimmed;
                        this.kill();
                    }
                }
                if (action.equals("c")) {
                    if (App.blue) {
                        App.lastBlueMove = "O-O";
                        App.lastRedMove = "";
                    } else {
                        App.lastRedMove = "O-O";
                    }
                } else if (action.equals("x") || action.equals("o")) {
                    if (App.blue) {
                        App.lastBlueMove = prefix + action + suffix;
                        App.lastRedMove = "";
                    } else {
                        App.lastRedMove = prefix + action + suffix;
                    }
                } else if (action.equals("") && this.type.equals("pawn")) {
                    if (App.blue) {
                        App.lastBlueMove = suffix;
                        App.lastRedMove = "";
                    } else {
                        App.lastRedMove = suffix;
                    }
                } else {
                    if (App.blue) {
                        App.lastBlueMove = prefix + action + suffix;
                        App.lastRedMove = "";
                    } else {
                        App.lastRedMove = prefix + action + suffix;
                    }
                }
                if (!App.pvp) {
                    if (App.blue) App.blue = false;
                } else {
                    App.blue = !App.blue;
                }
                App.selectedPiece = null;
                updateMaterial();
                updateVision();
            }
            x = this.xPos * App.pieceSize;
            y = this.yPos * App.pieceSize;
            App.selectedPiece = null;
            App.move++;
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
            ArrayList<int[]> moves = App.getAvailableSquares(pieces.get(i));
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

    public String getPrefix() {
        /*
        "raze",    "fade", "breach",    "reyna", "jett",   "phoenix", "skye",  "sage", "kayo",
                   "neon",                                                     "yoru",
        "chamber", "omen", "brimstone", "astra", "cypher", "killjoy", "viper", "OMEN", "sova"
        */
        if (this.type.equalsIgnoreCase("pawn")) {
            return "";
        } else if (this.agent.equalsIgnoreCase("uraze")) {
            return "RZ";
        } else if (this.agent.equalsIgnoreCase("ufade")) {
            return "FD";
        } else if (this.agent.equalsIgnoreCase("ubreach")) {
            return "BR";
        } else if (this.agent.equalsIgnoreCase("ureyna")) {
            return "RN";
        } else if (this.agent.equalsIgnoreCase("ujett")) {
            return "JT";
        } else if (this.agent.equalsIgnoreCase("uphoenix")) {
            return "PX";
        } else if (this.agent.equalsIgnoreCase("uskye")) {
            return "SK";
        } else if (this.agent.equalsIgnoreCase("usage")) {
            return "SG";
        } else if (this.agent.equalsIgnoreCase("kayo")) {
            return "KO";
        } else if (this.type.equalsIgnoreCase("bishop")) {
            return "NE";
        } else if (this.type.equalsIgnoreCase("rook")) {
            return "YR";
        } else if (this.agent.equalsIgnoreCase("chamber") || (this.agent.equalsIgnoreCase("uchamber"))) {
            return "CH";
        } else if (this.agent.equalsIgnoreCase("omen") || (this.agent.equalsIgnoreCase("uomen"))) {
            return "OM";
        } else if (this.agent.equalsIgnoreCase("brimstone")) {
            return "BM";
        } else if (this.agent.equalsIgnoreCase("astra")) {
            return "AS";
        } else if (this.agent.equalsIgnoreCase("cypher")) {
            return "CY";
        } else if (this.agent.equalsIgnoreCase("killjoy")) {
            return "KJ";
        } else if (this.agent.equalsIgnoreCase("viper")) {
            return "VP";
        } else {
            return "SV";
        }
    }

}
