import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class App extends JFrame {
    public static boolean blue = true;
    private boolean trueBlue = false;
    private int p1vision;
    private int p2vision;
    public static int blueMaterial = 0;
    public static int redMaterial = 0;
    public static boolean blueCastled = false;
    public static boolean redCastled = false;
    public static int[] lastDeparture = {10, 10};
    public static int[] lastDestination = {10, 10};
    public static int pieceSize = 64;
    public static Piece selectedPiece = null;
    public static ArrayList<Piece> pieces = new ArrayList<Piece>();

    JPanel gamePanel;
    JLabel blueMat;
    JLabel redMat;


    public App() throws IOException {

        // Constructing the GUI
        setSize(900, 900);
        setUndecorated(true);
        setVisible(true);
        
        // Red pawns
        new Piece("raze",      8, 2, false, false, pieces);
        new Piece("fade",      7, 2, false, false, pieces);
        new Piece("breach",    6, 2, false, false, pieces);
        new Piece("reyna",     5, 2, false, false, pieces);
        new Piece("jett",      4, 2, false, false, pieces);
        new Piece("phoenix",   3, 2, false, false, pieces);
        new Piece("skye",      2, 2, false, false, pieces);
        new Piece("sage",      1, 2, false, false, pieces);
        new Piece("kayo",      0, 2, false, false, pieces);
        // Red 2nd rank
        new Piece("neon",      7, 1, false, false, pieces);
        new Piece("yoru",      1, 1, false, false, pieces);
        // Red backrank
        new Piece("chamber",   8, 0, false, false, pieces);
        new Piece("omen",      7, 0, false, false, pieces);
        new Piece("brimstone", 6, 0, false, false, pieces);
        new Piece("astra",     5, 0, false, false, pieces);
        Piece rCypher =    new Piece("cypher",    4, 0, false, false, pieces);
        new Piece("killjoy",   3, 0, false, false, pieces);
        new Piece("viper",     2, 0, false, false, pieces);
        new Piece("OMEN",      1, 0, false, false, pieces);
        new Piece("sova",      0, 0, false, false, pieces);

        // Blue pawns
        new Piece("raze",      0, 6, true, false, pieces);
        new Piece("fade",      1, 6, true, false, pieces);
        new Piece("breach",    2, 6, true, false, pieces);
        new Piece("reyna",     3, 6, true, false, pieces);
        new Piece("jett",      4, 6, true, false, pieces);
        new Piece("phoenix",   5, 6, true, false, pieces);
        new Piece("skye",      6, 6, true, false, pieces);
        new Piece("sage",      7, 6, true, false, pieces);
        new Piece("kayo",      8, 6, true, false, pieces);
        // Blue 2nd rank
        new Piece("neon",      1, 7, true, false, pieces);
        new Piece("yoru",      7, 7, true, false, pieces);
        // Blue backrank
        new Piece("chamber",   0, 8, true, false, pieces);
        new Piece("omen",      1, 8, true, false, pieces);
        new Piece("brimstone", 2, 8, true, false, pieces);
        new Piece("astra",     3, 8, true, false, pieces);
        Piece bCypher =    new Piece("cypher",    4, 8, true, false, pieces);
        new Piece("killjoy",   5, 8, true, false, pieces);
        new Piece("viper",     6, 8, true, false, pieces);
        new Piece("OMEN",      7, 8, true, false, pieces);
        new Piece("sova",      8, 8, true, false, pieces);


        // Getting images for blue team pieces
        BufferedImage blue = ImageIO.read(getInputStream(new URL("https://cdn.discordapp.com/attachments/873323229979230258/1005952553621663835/blueTeamm.png")));
        Image bluePieces[] = new Image[20];
        //ImageIcon bluePiecesI[] = new ImageIcon[20];
        //JLabel bluePiecesL[] = new JLabel[20];
        int c = 0;
        for (int y = 0; y < 300; y += 100) {
            for (int x = 0; x < 900; x += 100) {
                if (y == 100 && x > 100) continue; // 2nd row only has 2 pieces
                bluePieces[c] = blue.getSubimage(x, y, 100, 100).getScaledInstance(pieceSize, pieceSize, BufferedImage.SCALE_SMOOTH);
                //bluePiecesI[c] = new ImageIcon(bluePieces[c]);
                //bluePiecesL[c] = new JLabel(bluePiecesI[c]);
                c++;
            }
        }

        // Getting images for red team pieces
        Image redPieces[] = new Image[20];
        if (trueBlue) {
            BufferedImage red = ImageIO.read(getInputStream(new URL("https://cdn.discordapp.com/attachments/873323229979230258/1005952555618156696/redTeamF.png")));
            c = 0;
            for (int y = 200; y >= 0; y -= 100) {
                for (int x = 800; x >=0; x -= 100) {
                    if (y == 100 && x < 700) continue; // 2nd row only has 2 pieces
                    redPieces[c] = red.getSubimage(x, y, 100, 100).getScaledInstance(pieceSize, pieceSize, BufferedImage.SCALE_SMOOTH);
                    c++;
                }
            }
        } else {
            BufferedImage red = ImageIO.read(getInputStream(new URL("https://cdn.discordapp.com/attachments/873323229979230258/1005952555928530944/redTeamm.png")));
            c = 0;
            for (int y = 0; y < 300; y += 100) {
                for (int x = 0; x < 900; x += 100) {
                    if (y == 100 && x > 100) continue; // 2nd row only has 2 pieces
                    redPieces[c] = red.getSubimage(x, y, 100, 100).getScaledInstance(pieceSize, pieceSize, BufferedImage.SCALE_SMOOTH);
                    c++;
                }
            }
        }

        Image misc[] = new Image[4];
        BufferedImage bMisc = ImageIO.read(getInputStream(new URL("https://cdn.discordapp.com/attachments/873323229979230258/1005952554955444355/misc.png")));
        c = 0;
        for (int x = 0; x < 400; x += 100) {
            misc[c] = bMisc.getSubimage(x, 0, 100, 100).getScaledInstance(pieceSize, pieceSize, BufferedImage.SCALE_SMOOTH);
            c++;
        }

        // Board image
        BufferedImage image = ImageIO.read(getInputStream(new URL("https://cdn.discordapp.com/attachments/873323229979230258/1005952553919447070/board.png")));
        Image imagee = (image.getScaledInstance(pieceSize*9, pieceSize*9, BufferedImage.SCALE_SMOOTH));

        // GG image
        BufferedImage gg = ImageIO.read(getInputStream(new URL("https://cdn.discordapp.com/attachments/873323229979230258/1005952554221453433/gg.png")));

        // Drawing the game panel and starting position
        gamePanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagee, 0, 0, null);

                if (isChecked(true) && selectedPiece != bCypher) {
                    g.drawImage(misc[2], bCypher.x, bCypher.y, this);
                } else if (isChecked(false) && selectedPiece != rCypher) {
                    g.drawImage(misc[2], rCypher.x, rCypher.y, this);
                }

                if (selectedPiece != null) {
                    for (int[] move : getAvailableSquares(selectedPiece)) {
                        if (killedPiece(move[0], move[1], selectedPiece.isBlue) != null) {
                            g.drawImage(misc[0], move[0]*pieceSize, move[1]*pieceSize, this);
                        } else {
                            g.drawImage(misc[3], move[0]*pieceSize, move[1]*pieceSize, this);
                        }
                    }
                }

                if (lastDeparture[0] < 10 && lastDestination[0] < 10) {
                    g.drawImage(misc[1], lastDeparture[0]*pieceSize, lastDeparture[1]*pieceSize, this);
                    g.drawImage(misc[1], lastDestination[0]*pieceSize, lastDestination[1]*pieceSize, this);
                }

                for (Piece p: pieces) {
                    List<String> agents = Arrays.asList("raze", "fade", "breach", "reyna", "jett", "phoenix", "skye", "sage", "kayo",
                    "neon", "yoru",
                    "chamber", "omen", "brimstone", "astra", "cypher", "killjoy", "viper", "OMEN", "sova");
                    int i = agents.indexOf(p.agent);
                    if (p.isBlue && p != selectedPiece) {
                        g.drawImage(bluePieces[i], p.x, p.y, this);
                    } else if (!p.isBlue && p != selectedPiece) {
                        g.drawImage(redPieces[i], p.x, p.y, this);
                    }
                    
                }
                for (Piece p: pieces) {
                    List<String> agents = Arrays.asList("raze", "fade", "breach", "reyna", "jett", "phoenix", "skye", "sage", "kayo",
                    "neon", "yoru",
                    "chamber", "omen", "brimstone", "astra", "cypher", "killjoy", "viper", "OMEN", "sova");
                    int i = agents.indexOf(p.agent);
                    if (p.isBlue && p == selectedPiece) {
                        g.drawImage(bluePieces[i], p.x, p.y, this);
                    } else if (!p.isBlue && p == selectedPiece) {
                        g.drawImage(redPieces[i], p.x, p.y, this);
                    }
                }
                if (isCheckmated(App.blue)) g.drawImage(gg.getScaledInstance(pieceSize*9, pieceSize*9, BufferedImage.SCALE_SMOOTH), 0, 0, null);
            }
        };
        /*
        blueMat = new JLabel(String.valueOf(blueMaterial-redMaterial));
        redMat = new JLabel(String.valueOf(redMaterial-blueMaterial));
        blueMat.setLocation(8*pieceSize, 8*pieceSize);
        redMat.setLocation(8*pieceSize, 0);
        gamePanel.add(blueMat);
        gamePanel.add(redMat); */
        add(gamePanel);
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedPiece != null) {
                    selectedPiece.x = e.getX() - pieceSize/2;
                    selectedPiece.y = (int)(e.getY() - pieceSize/1.2);
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //System.out.println((getPiece(e.getX(), e.getY()).isBlue?"blue ":"red ")+getPiece(e.getX(), e.getY()).agent);
                if (getPiece(e.getX(), e.getY()) != null && getPiece(e.getX(), e.getY()).isBlue == App.blue) {
                    selectedPiece = getPiece(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedPiece.move(e.getX()/pieceSize, e.getY()/pieceSize, false);
                repaint();
                if(isCheckmated(App.blue)) System.out.println("gg");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
       
        setDefaultCloseOperation(3);
        setVisible(true);
    }
    public static Piece getPiece(int x,int y){
        int xPos = x/pieceSize;
        int yPos = y/pieceSize;
        for(Piece p: pieces){
            if(p.xPos == xPos && p.yPos == yPos) {
                return p;
            }
        }
        return null;
    }

    public static Piece killedPiece(int x,int y, boolean blue){
        ArrayList<Piece> ps= new ArrayList<Piece>();
        for(Piece p: pieces){
            if(p.xPos == x && p.yPos == y) {
                if (p != null) ps.add(p);
            }
        }
        for (int i = 0; i < ps.size(); i++) {
            if(ps.get(i).isBlue != blue) {
                return(ps.get(i));
            }
        }
        return(null);
    }

    public static int[][] getRawMoves(Piece piece) {
        if (piece.getType().equalsIgnoreCase("knight")) {
            int[] xMoves = {-2, -2, -1, +1, +2, +2, +1, -1};
            int[] yMoves = {-1, +1, +2, +2, +1, -1, -2, -2};
            return(new int[][]{xMoves, yMoves});
        } else if (piece.getType().equalsIgnoreCase("pawn")) {
            int[] xMoves = {0, 0, 0};
            int[] yMoves = {0, 0, 0};
            int mod;
            if (piece.isBlue) {
                mod = -1;
            } else {
                mod = 1;
            }
            if (getPiece(piece.xPos*pieceSize, piece.yPos*pieceSize + pieceSize*mod) == null) {
                yMoves[0] = mod;
            }
            else yMoves[0] = 20;
            yMoves[1] = mod;
            yMoves[2] = mod;

            int xKekW = pawnCanAttack(piece);
            if (xKekW == 2) {
                //System.out.println("attac 2");
                xMoves[1] = 1;
                xMoves[2] = -1;
            } else if (xKekW == 0) {
                //System.out.println("attac 0");
                xMoves[1] = xKekW;
                xMoves[2] = xKekW;
                yMoves[1] = 20;
                yMoves[2] = 20;
            } else {
                //System.out.println("attac " + xKekW);
                xMoves[1] = xKekW;
                xMoves[2] = xKekW;
            }

            return(new int[][]{xMoves, yMoves});
        } else if (piece.getType().equalsIgnoreCase("lance")) {
            int[] xMoves = {-1, +1};
            int[] yMoves = {0 ,  0};
            return(new int[][]{xMoves, yMoves});
        } else if (piece.getType().equalsIgnoreCase("king")) {
            int[] xMoves = {-1, -1, -1,  0,  0, +1, +1, +1, +1, +1};
            int[] yMoves = {-1,  0, +1, +1, -1, -1,  0, +1,  0,  0};
            if (blue && canCastleChamber(blue)) {
                xMoves[8] = -2;
            }
            if (blue && canCastleSova(blue)) {
                xMoves[9] = +2;
            }
            else if (!blue && canCastleChamber(blue)) {
                xMoves[8] = +2;
            }
            if (!blue && canCastleSova(blue)) {
                xMoves[9] = -2;
            }
            return(new int[][]{xMoves, yMoves});
        } else if (piece.getType().equalsIgnoreCase("silver")) {
            int[] xMoves = {-1, -1,  0, +1, +1};
            int[] yMoves = new int[5];
            int mod;
            if (piece.isBlue) mod = 1;
            else mod = -1;
            // {-1, +1, -1, +1, -1}
            yMoves[0] = -1*mod;
            yMoves[1] = 1*mod;
            yMoves[2] = -1*mod;
            yMoves[3] = 1*mod;
            yMoves[4] = -1*mod;
            return(new int[][]{xMoves, yMoves});
        } else if (piece.getType().equalsIgnoreCase("rook")){
            int x;
            int y;
            ArrayList<Integer> xMoveList = new ArrayList<Integer>();
            ArrayList<Integer> yMoveList = new ArrayList<Integer>();
            x = piece.xPos;
            y = piece.yPos + 1;
            while (x < 9 && x >= 0 && y < 9 && y >= 0) {
                if (getPiece(x*pieceSize, y*pieceSize) == null) yMoveList.add(y-piece.yPos);
                else if (getPiece(x*pieceSize, y*pieceSize).isBlue != piece.isBlue) {
                    yMoveList.add(y-piece.yPos);
                    break;
                } else {
                    break;
                }
                y++;
            }
            x = piece.xPos;
            y = piece.yPos - 1;
            while (x < 9 && x >= 0 && y < 9 && y >= 0) {
                if (getPiece(x*pieceSize, y*pieceSize) == null) yMoveList.add(y-piece.yPos);
                else if (getPiece(x*pieceSize, y*pieceSize).isBlue != piece.isBlue) {
                    yMoveList.add(y-piece.yPos);
                    break;
                } else {
                    break;
                }
                y--;
            }
            x = piece.xPos + 1;
            y = piece.yPos;
            while (x < 9 && x >= 0 && y < 9 && y >= 0) {
                if (getPiece(x*pieceSize, y*pieceSize) == null) xMoveList.add(x-piece.xPos);
                else if (getPiece(x*pieceSize, y*pieceSize).isBlue != piece.isBlue) {
                    xMoveList.add(x-piece.xPos);
                    break;
                } else {
                    break;
                }
                x++;
            }
            x = piece.xPos - 1;
            y = piece.yPos;
            while (x < 9 && x >= 0 && y < 9 && y >= 0) {
                if (getPiece(x*pieceSize, y*pieceSize) == null) xMoveList.add(x-piece.xPos);
                else if (getPiece(x*pieceSize, y*pieceSize).isBlue != piece.isBlue) {
                    xMoveList.add(x-piece.xPos);
                    break;
                } else {
                    break;
                }
                x--;
            }
            int[] xMoves = new int[xMoveList.size() + yMoveList.size()];
            int[] yMoves = new int[yMoveList.size() + xMoveList.size()];
            for (int i = 0; i < xMoveList.size(); i++) {
                xMoves[i] = xMoveList.get(i);
                yMoves[i] = 0;
            }
            for (int i = xMoveList.size(); i < xMoveList.size()+yMoveList.size(); i++) {
                yMoves[i] = yMoveList.get(i-xMoveList.size());
                xMoves[i] = 0;
            }
            /* 
            int[] yMoves = {+1, -1,  0,  0};
            int[] xMoves = { 0,  0, -1, +1};*/
            return(new int[][]{xMoves, yMoves});           
        } else { // Bishop
            /*
            int[] yMoves = {+1, -1, +1, -1};
            int[] xMoves = {+1, -1, -1, +1}*/

            int x;
            int y;
            ArrayList<Integer> xMoveList = new ArrayList<Integer>();
            ArrayList<Integer> yMoveList = new ArrayList<Integer>();
            x = piece.xPos + 1;
            y = piece.yPos + 1;
            while (x < 9 && x >= 0 && y < 9 && y >= 0) {
                if (getPiece(x*pieceSize, y*pieceSize) == null) {
                    yMoveList.add(y-piece.yPos);
                    xMoveList.add(x-piece.xPos);
                }
                else if (getPiece(x*pieceSize, y*pieceSize).isBlue != piece.isBlue) {
                    yMoveList.add(y-piece.yPos);
                    xMoveList.add(x-piece.xPos);
                    break;
                } else {
                    break;
                }
                y++;
                x++;
            }
            x = piece.xPos + 1;
            y = piece.yPos - 1;
            while (x < 9 && x >= 0 && y < 9 && y >= 0) {
                if (getPiece(x*pieceSize, y*pieceSize) == null) {
                    yMoveList.add(y-piece.yPos);
                    xMoveList.add(x-piece.xPos);
                }
                else if (getPiece(x*pieceSize, y*pieceSize).isBlue != piece.isBlue) {
                    yMoveList.add(y-piece.yPos);
                    xMoveList.add(x-piece.xPos);
                    break;
                } else {
                    break;
                }
                y--;
                x++;
            }
            x = piece.xPos - 1;
            y = piece.yPos + 1;
            while (x < 9 && x >= 0 && y < 9 && y >= 0) {
                if (getPiece(x*pieceSize, y*pieceSize) == null) {
                    yMoveList.add(y-piece.yPos);
                    xMoveList.add(x-piece.xPos);
                }
                else if (getPiece(x*pieceSize, y*pieceSize).isBlue != piece.isBlue) {
                    yMoveList.add(y-piece.yPos);
                    xMoveList.add(x-piece.xPos);
                    break;
                } else {
                    break;
                }
                y++;
                x--;
            }
            x = piece.xPos - 1;
            y = piece.yPos - 1;
            while (x < 9 && x >= 0 && y < 9 && y >= 0) {
                if (getPiece(x*pieceSize, y*pieceSize) == null) {
                    yMoveList.add(y-piece.yPos);
                    xMoveList.add(x-piece.xPos);
                }
                else if (getPiece(x*pieceSize, y*pieceSize).isBlue != piece.isBlue) {
                    yMoveList.add(y-piece.yPos);
                    xMoveList.add(x-piece.xPos);
                    break;
                } else {
                    break;
                }
                y--;
                x--;
            }
            int[] xMoves = new int[xMoveList.size()];
            int[] yMoves = new int[yMoveList.size()];
            for (int i = 0; i < xMoveList.size(); i++) {
                xMoves[i] = xMoveList.get(i);
                yMoves[i] = yMoveList.get(i);
            }
            /* 
            int[] yMoves = {+1, -1,  0,  0};
            int[] xMoves = { 0,  0, -1, +1};*/
            return(new int[][]{xMoves, yMoves});           

        }
    }

    public static ArrayList<int[]> getAvailableSquares(Piece piece) {
        //System.out.println("we at " + piece.xPos + "," + piece.yPos);
        ArrayList<int[]> availableSquares = new ArrayList<int[]>();
        int[][] moves = getRawMoves(piece);
        int[] xMoves = moves[0];
        int[] yMoves = moves[1];
        for (int i = 0; i < yMoves.length; i++) {
            int x = piece.xPos;
            int y = piece.yPos;
            x += xMoves[i];
            y += yMoves[i];
            if (occupied(piece.isBlue, x, y)) {
                //System.out.println("occ " + x + "," + y);
                continue;
            }
            if (!inBounds(x, y)) {
                //System.out.println("bound " + x + "," + y);
                continue;
            }
            if (!occupied(piece.isBlue, x, y) && inBounds(x, y)) {
                int oldx = -1;
                int oldy = -1;
                if (killedPiece(x, y, piece.isBlue) != null) {
                    oldx = killedPiece(x, y, piece.isBlue).xPos;
                    oldy = killedPiece(x, y, piece.isBlue).yPos;
                    //System.out.println("moving " + killedPiece(x, y, piece.isBlue).agent + " @ " + x + "," + y);
                    killedPiece(x, y, piece.isBlue).move(10, 10, true);
                    
                }
                piece.move(x, y, true);
                if (!isChecked(piece.isBlue)) {
                    availableSquares.add(new int[]{x, y});
                } else System.out.println("chec");
                piece.move(x-xMoves[i], y-yMoves[i], true);
                if (getPiece(10*pieceSize, 10*pieceSize) != null) getPiece(10*pieceSize, 10*pieceSize).move(oldx, oldy, true);
            }
        }

        return(availableSquares);
    }

    public static boolean inBounds(int x, int y) {
        if (x < 9 && x >= 0 && y < 9 && y >= 0) return true;
        else return false;
    }

    public static boolean occupied(boolean blue, int x, int y) {
        if (getPiece(x*pieceSize, y*pieceSize) == null) return false;
        if (blue == getPiece(x*pieceSize, y*pieceSize).isBlue) {
            return true;
        } else return false;
    }

    public static boolean isChecked(boolean blue) {
        int yMod;
        if (blue) yMod = -1;
        else yMod = 1;
        // Find the king on the board
        Piece p = pieces.stream().filter((s) -> (s.isBlue == blue) && s != null && s.type.equalsIgnoreCase("king")).findFirst().orElse(null);
        // If the king is checked by a lance
        int x = p.xPos;
        int y = p.yPos + yMod;
        while (y < 9 && y >= 0) {
            if (getPiece(x*pieceSize, y*pieceSize) == null) {
                y+=yMod;
                continue;
            }
            else if (getPiece(x*pieceSize, y*pieceSize).isBlue != blue && (getPiece(x*pieceSize, y*pieceSize).getType().equalsIgnoreCase("lance") || getPiece(x*pieceSize, y*pieceSize).getType().equalsIgnoreCase("rook"))) {
                return(true);
            } else {
                break;
            }
        }
        // If the king is checked by a rook
        x = p.xPos;
        y = p.yPos - yMod;
        while ((y < 9 && y >= 0) && (x < 9 && x >= 0)) {
            if (getPiece(x*pieceSize, y*pieceSize) == null) {
                y-=yMod;
                continue;
            }
            else if (getPiece(x*pieceSize, y*pieceSize).isBlue != blue && (getPiece(x*pieceSize, y*pieceSize).getType().equalsIgnoreCase("rook"))) {
                return(true);
            } else {
                break;
            }
        }
        x = p.xPos+1;
        y = p.yPos;
        while ((y < 9 && y >= 0) && (x < 9 && x >= 0)) {
            if (getPiece(x*pieceSize, y*pieceSize) == null) {
                x++;
                continue;
            }
            else if (getPiece(x*pieceSize, y*pieceSize).isBlue != blue && (getPiece(x*pieceSize, y*pieceSize).getType().equalsIgnoreCase("rook"))) {
                return(true);
            } else {
                break;
            }
        }
        x = p.xPos-1;
        y = p.yPos;
        while ((y < 9 && y >= 0) && (x < 9 && x >= 0)) {
            if (getPiece(x*pieceSize, y*pieceSize) == null) {
                x--;
                continue;
            }
            else if (getPiece(x*pieceSize, y*pieceSize).isBlue != blue && (getPiece(x*pieceSize, y*pieceSize).getType().equalsIgnoreCase("rook"))) {
                return(true);
            } else {
                break;
            }
        }
        // If the king is checked by a bishop
        x = p.xPos + 1;
        y = p.yPos + 1;
        while ((y < 9 && y >= 0) && (x < 9 && x >= 0)) {
            if (getPiece(x*pieceSize, y*pieceSize) == null) {
                x++;
                y++;
                continue;
            } else if (getPiece(x*pieceSize, y*pieceSize).isBlue != blue && (getPiece(x*pieceSize, y*pieceSize).getType().equalsIgnoreCase("bishop"))) {
                return(true);
            } else {
                break;
            }
        }
        x = p.xPos - 1;
        y = p.yPos + 1;
        while ((y < 9 && y >= 0) && (x < 9 && x >= 0)) {
            if (getPiece(x*pieceSize, y*pieceSize) == null) {
                x--;
                y++;
                continue;
            } else if (getPiece(x*pieceSize, y*pieceSize).isBlue != blue && (getPiece(x*pieceSize, y*pieceSize).getType().equalsIgnoreCase("bishop"))) {
                return(true);
            } else {
                break;
            }
        }
        x = p.xPos + 1;
        y = p.yPos - 1;
        while ((y < 9 && y >= 0) && (x < 9 && x >= 0)) {
            if (getPiece(x*pieceSize, y*pieceSize) == null) {
                x++;
                y--;
                continue;
            } else if (getPiece(x*pieceSize, y*pieceSize).isBlue != blue && (getPiece(x*pieceSize, y*pieceSize).getType().equalsIgnoreCase("bishop"))) {
                return(true);
            } else {
                break;
            }
        }
        x = p.xPos - 1;
        y = p.yPos - 1;
        while ((y < 9 && y >= 0) && (x < 9 && x >= 0)) {
            if (getPiece(x*pieceSize, y*pieceSize) == null) {
                x--;
                y--;
                continue;
            } else if (getPiece(x*pieceSize, y*pieceSize).isBlue != blue && (getPiece(x*pieceSize, y*pieceSize).getType().equalsIgnoreCase("bishop"))) {
                return(true);
            } else {
                break;
            }
        }
        // If the king is checked by a knight
        x = p.xPos;
        y = p.yPos;
        int[] knightX = {-2, -2, -1, +1, +2, +2, +1, -1};
        int[] knightY = {-1, +1, +2, +2, +1, -1, -2, -2};
        for (int i = 0; i < knightX.length; i++) {
            if (getPiece((x+knightX[i])*pieceSize, (y+knightY[i])*pieceSize) == null) continue;
            else if (getPiece((x+knightX[i])*pieceSize, (y+knightY[i])*pieceSize) != null && getPiece((x+knightX[i])*pieceSize, (y+knightY[i])*pieceSize).getType().equalsIgnoreCase("knight") && getPiece((x+knightX[i])*pieceSize, (y+knightY[i])*pieceSize).isBlue != blue) {
                return(true);
            }
        }
        // If the king is checked by a silver
        x = p.xPos;
        y = p.yPos;
        int[] silverX = {-1, -1,  0, +1, +1};
        int[] silverY = new int[5];
        int mod = -yMod;
        // {-1, +1, -1, +1, -1}
        silverY[0] = -1*mod;
        silverY[1] =  1*mod;
        silverY[2] = -1*mod;
        silverY[3] =  1*mod;
        silverY[4] = -1*mod;

        for (int i = 0; i < silverX.length; i++) {
            if (getPiece((x+silverX[i])*pieceSize, (y+silverY[i])*pieceSize) == null) continue;
            if (getPiece((x+silverX[i])*pieceSize, (y+silverY[i])*pieceSize) != null && getPiece((x+silverX[i])*pieceSize, (y+silverY[i])*pieceSize).getType().equalsIgnoreCase("silver") && getPiece((x+silverX[i])*pieceSize, (y+silverY[i])*pieceSize).isBlue != blue) {
                return(true);
            }
        }
        // If the king is checked by a pawn
        List<Piece> pawns = pieces.stream().filter((pi) -> (pi.type.equalsIgnoreCase("pawn") && pi.isBlue != blue)).collect(Collectors.toList());
        for (int index = 0; index < pawns.size(); index++) {
            if (pawnCheck(pawns.get(index))) return true;            
        }

        return(false);
    }
    
    public static int pawnCanAttack(Piece piece) {
        int mod;
        if (piece.isBlue) mod = -1;
        else mod = 1;
        if ((getPiece(piece.xPos*pieceSize + pieceSize, piece.yPos*pieceSize + pieceSize*mod) != null && getPiece(piece.xPos*pieceSize + pieceSize, piece.yPos*pieceSize + pieceSize*mod).isBlue != piece.isBlue) && (getPiece(piece.xPos*pieceSize - pieceSize, piece.yPos*pieceSize + pieceSize*mod) != null && getPiece(piece.xPos*pieceSize - pieceSize, piece.yPos*pieceSize + pieceSize*mod).isBlue != piece.isBlue)) {
            return 2;
        } else if (getPiece(piece.xPos*pieceSize + pieceSize, piece.yPos*pieceSize + pieceSize*mod) != null && getPiece(piece.xPos*pieceSize + pieceSize, piece.yPos*pieceSize + pieceSize*mod).isBlue != piece.isBlue) {
            return +1;
        } else if (getPiece(piece.xPos*pieceSize - pieceSize, piece.yPos*pieceSize + pieceSize*mod) != null && getPiece(piece.xPos*pieceSize - pieceSize, piece.yPos*pieceSize + pieceSize*mod).isBlue != piece.isBlue) {
            return -1;
        } else {
            return 0;
        }
    }

    public static boolean pawnCheck(Piece piece) {
        int mod;
        if (piece.isBlue) mod = -1;
        else mod = 1;
        Piece p1 = getPiece(piece.xPos*pieceSize + pieceSize, piece.yPos*pieceSize + pieceSize*mod);
        Piece p2 = getPiece(piece.xPos*pieceSize - pieceSize, piece.yPos*pieceSize + pieceSize*mod);
        if ((p1 != null && p1.isBlue != piece.isBlue && p1.type.equalsIgnoreCase("king")) || (p2 != null && p2.isBlue != piece.isBlue && p2.type.equalsIgnoreCase("king"))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean canCastleChamber(boolean blue) {
        if (blue && blueCastled) return false;
        if (!blue && redCastled) return false;
        boolean castle = true;
        int rank;
        int file;
        if (blue) {
            rank = 8;
            file = 0;
        }
        else {
            rank = 0;
            file = 8;
        }
        Piece p = pieces.stream().filter((s) -> (s.isBlue == blue) && s != null && s.type.equalsIgnoreCase("king")).findFirst().orElse(null);
        Piece c = pieces.stream().filter((s) -> (s.isBlue == blue) && s != null && s.agent.equalsIgnoreCase("chamber")).findFirst().orElse(null);
        if (c == null) return false;
        if (p.yPos != rank || p.xPos != 4) castle = false;
        if (c.yPos != rank || c.xPos != file) castle = false;
        if (blue) {
            if (getPiece(1*pieceSize, 8*pieceSize) != null || getPiece(2*pieceSize, 8*pieceSize) != null || getPiece(3*pieceSize, 8*pieceSize) != null) {
                castle = false;
            }
        } else {
            if (getPiece(7*pieceSize, 0) != null || getPiece(6*pieceSize, 0) != null || getPiece(5*pieceSize, 0) != null) {
                castle = false;
            } 
        }
        return castle;
    }

    public static boolean canCastleSova(boolean blue) {
        if (blue && blueCastled) return false;
        if (!blue && redCastled) return false;
        boolean castle = true;
        int rank;
        int file;
        if (blue) {
            rank = 8;
            file = 8;
        }
        else {
            rank = 0;
            file = 0;
        }
        Piece p = pieces.stream().filter((s) -> (s.isBlue == blue) && s != null && s.type.equalsIgnoreCase("king")).findFirst().orElse(null);
        Piece c = pieces.stream().filter((s) -> (s.isBlue == blue) && s != null && s.agent.equalsIgnoreCase("sova")).findFirst().orElse(null);
        if (c == null) return false;
        if (p.yPos != rank || p.xPos != 4) {
            castle = false;
            // System.out.println("king is at " + p.xPos + "," + p.yPos);
        }
        if (c.yPos != rank || c.xPos != file) {
            castle = false;
            // System.out.println("lance is at " + c.xPos + "," + c.yPos);
        }
        if (blue) {
            if (getPiece(7*pieceSize, 8*pieceSize) != null || getPiece(6*pieceSize, 8*pieceSize) != null || getPiece(5*pieceSize, 8*pieceSize) != null) {
                castle = false;
            }
        } else {
            if (getPiece(1*pieceSize, 0) != null || getPiece(2*pieceSize, 0) != null || getPiece(3*pieceSize, 0) != null) {
                castle = false;
            } 
        }
        return castle;
    }

    public static boolean isCheckmated(boolean blue) {
        if (!isChecked(blue)) {
            return false;
        } else {
            List<Piece> p = pieces.stream().filter((s) -> (s.isBlue == blue)).collect(Collectors.toList());
            for (int i = 0; i < p.size(); i++) {
                if (getAvailableSquares(p.get(i)).size() > 0) return false;
            }
            return true;
        }
    }

    public InputStream getInputStream(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        connection.connect();
        return(connection.getInputStream());
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
    }
}
