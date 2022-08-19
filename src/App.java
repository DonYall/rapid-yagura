import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class App extends JFrame {
    int posX = 0, posY = 0;
    private int port;
    private ClientSideConnection csc;
    public static int playerID;
    public static boolean simple= true;
    public static int move = 0;
    public static boolean blue = true;
    public static boolean turn = false;
    public static boolean pvp = true;
    Image bluePieces[] = new Image[20];
    Image redPieces[] = new Image[20];
    public static float blueVision;
    public static float redVision;
    public static int blueMaterial = 0;
    public static int redMaterial = 0;
    public static boolean blueCastled = false;
    public static boolean redCastled = false;
    public static int[] lastDeparture = {10, 10};
    public static int[] lastDestination = {10, 10};
    public static int pieceSize = 64;
    public static String lastBlueMove = "";
    public static String lastRedMove = "";
    public static String opening1 = "";
    public static String opening2 = "";
    public static String opening3 = "";
    public static Piece selectedPiece = null;
    public static ArrayList<Piece> pieces = new ArrayList<Piece>();
    public static ArrayList<DeadPiece> deadPieces = new ArrayList<DeadPiece>();
    public static ArrayList<Piece> stunnedPieces = new ArrayList<Piece>(); 
    public static boolean search = false;
    public static Piece blueBoom = null;
    public static Piece redBoom = null;
    public static int[] bluePhoenix = {10, 10};
    public static int[] redPhoenix = {10, 10};
    public static URL moveURL;
    public static URL captureURL;
    public static URL sageURL;
    public static URL satchelURL;
    public static URL tntURL;
    public static URL obliteratedURL;
    public static URL phoenixURL;
    public static URL scatterURL;
    public static URL[] laughURL = new URL[2];
    public static URL reynaURL;
    public static URL breachURL;

    JPanel gamePanel;

    public App(int port) throws Exception {

        this.port = port;

        moveURL = App.class.getResource("move.wav");
        captureURL = App.class.getResource("capture.wav");
        sageURL = App.class.getResource("sage.wav");
        satchelURL = App.class.getResource("satchel.wav");
        tntURL = App.class.getResource("tnt.wav");
        obliteratedURL = App.class.getResource("obliterated.wav");
        phoenixURL = App.class.getResource("phoenix.wav");
        scatterURL = App.class.getResource("scatter.wav");
        laughURL[0] = App.class.getResource("laugh1.wav");
        laughURL[1] = App.class.getResource("laugh2.wav");
        reynaURL = App.class.getResource("reyna.wav");
        breachURL = App.class.getResource("breach.wav");

        // Constructing the GUI
        setSize((int) (pieceSize * 9.5 * 16/9) , (int) (pieceSize * 9.5));
        setUndecorated(true);
        setVisible(true);

        // Red pawns
        new Piece("raze",      8, 2, false, pieces);
        new Piece("fade",      7, 2, false, pieces);
        new Piece("breach",    6, 2, false, pieces);
        new Piece("reyna",     5, 2, false, pieces);
        new Piece("jett",      4, 2, false, pieces);
        new Piece("phoenix",   3, 2, false, pieces);
        new Piece("skye",      2, 2, false, pieces);
        new Piece("sage",      1, 2, false, pieces);
        new Piece("kayo",      0, 2, false, pieces);
        // Red 2nd rank
        new Piece("neon",      7, 1, false, pieces);
        new Piece("yoru",      1, 1, false, pieces);
        // Red backrank
        new Piece("chamber",   8, 0, false, pieces);
        new Piece("omen",      7, 0, false, pieces);
        new Piece("brimstone", 6, 0, false, pieces);
        new Piece("astra",     5, 0, false, pieces);
        Piece rCypher =    new Piece("cypher",    4, 0, false, pieces);
        new Piece("killjoy",   3, 0, false, pieces);
        new Piece("viper",     2, 0, false, pieces);
        new Piece("OMEN",      1, 0, false, pieces);
        new Piece("sova",      0, 0, false, pieces);

        // Blue pawns
        new Piece("raze",      0, 6, true, pieces);
        new Piece("fade",      1, 6, true, pieces);
        new Piece("breach",    2, 6, true, pieces);
        new Piece("reyna",     3, 6, true, pieces);
        new Piece("jett",      4, 6, true, pieces);
        new Piece("phoenix",   5, 6, true, pieces);
        new Piece("skye",      6, 6, true, pieces);
        new Piece("sage",      7, 6, true, pieces);
        new Piece("kayo",      8, 6, true, pieces);
        // Blue 2nd rank
        new Piece("neon",      1, 7, true, pieces);
        new Piece("yoru",      7, 7, true, pieces);
        // Blue backrank
        new Piece("chamber",   0, 8, true, pieces);
        new Piece("omen",      1, 8, true, pieces);
        new Piece("brimstone", 2, 8, true, pieces);
        new Piece("astra",     3, 8, true, pieces);
        Piece bCypher =    new Piece("cypher",    4, 8, true, pieces);
        new Piece("killjoy",   5, 8, true, pieces);
        new Piece("viper",     6, 8, true, pieces);
        new Piece("OMEN",      7, 8, true, pieces);
        new Piece("sova",      8, 8, true, pieces);


        // Getting images for blue team pieces
        //BufferedImage blue = ImageIO.read(getInputStream(new URL("https://cdn.discordapp.com/attachments/873323229979230258/1005952553621663835/blueTeamm.png")));
        BufferedImage blue = ImageIO.read(App.class.getResource("blueTeamm.png"));
        Image bluePiecesW[] = new Image[20];
        //ImageIcon bluePiecesI[] = new ImageIcon[20];
        //JLabel bluePiecesL[] = new JLabel[20];
        int c = 0;
        for (int y = 0; y < 300; y += 100) {
            for (int x = 0; x < 900; x += 100) {
                if (y == 100 && x > 100) continue; // 2nd row only has 2 pieces
                bluePiecesW[c] = blue.getSubimage(x, y, 100, 100).getScaledInstance(pieceSize, pieceSize, BufferedImage.SCALE_SMOOTH);
                //bluePiecesI[c] = new ImageIcon(bluePieces[c]);
                //bluePiecesL[c] = new JLabel(bluePiecesI[c]);
                c++;
            }
        }
        BufferedImage blueF = ImageIO.read(App.class.getResource("blueTeammF.png"));
        Image bluePiecesF[] = new Image[20];
        c = 0;
        for (int y = 0; y < 300; y += 100) {
            for (int x = 0; x < 900; x += 100) {
                if (y == 100 && x > 100) continue; // 2nd row only has 2 pieces
                bluePiecesF[c] = blueF.getSubimage(x, y, 100, 100).getScaledInstance(pieceSize, pieceSize, BufferedImage.SCALE_SMOOTH);
                //bluePiecesI[c] = new ImageIcon(bluePieces[c]);
                //bluePiecesL[c] = new JLabel(bluePiecesI[c]);
                c++;
            }
        }

        // Getting images for red team pieces
        Image redPiecesW[] = new Image[20];
        //BufferedImage red = ImageIO.read(getInputStream(new URL("https://cdn.discordapp.com/attachments/873323229979230258/1005952555928530944/redTeamm.png")));
        BufferedImage red = ImageIO.read(App.class.getResource("redTeamm.png"));
        c = 0;
        for (int y = 0; y < 300; y += 100) {
            for (int x = 0; x < 900; x += 100) {
                if (y == 100 && x > 100) continue; // 2nd row only has 2 pieces
                redPiecesW[c] = red.getSubimage(x, y, 100, 100).getScaledInstance(pieceSize, pieceSize, BufferedImage.SCALE_SMOOTH);
                c++;
            }
        }
        BufferedImage redF = ImageIO.read(App.class.getResource("redTeammF.png"));
        Image redPiecesF[] = new Image[20];
        c = 0;
        for (int y = 0; y < 300; y += 100) {
            for (int x = 0; x < 900; x += 100) {
                if (y == 100 && x > 100) continue; // 2nd row only has 2 pieces
                redPiecesF[c] = redF.getSubimage(x, y, 100, 100).getScaledInstance(pieceSize, pieceSize, BufferedImage.SCALE_SMOOTH);
                c++;
            }
        }

        Image misc[] = new Image[12];
        BufferedImage bMisc = ImageIO.read(App.class.getResource("misc.png"));
        //BufferedImage bMisc = ImageIO.read(new File("Assets/misc.png"));
        c = 0;
        for (int x = 0; x < misc.length*100; x += 100) {
            misc[c] = bMisc.getSubimage(x, 0, 100, 100).getScaledInstance(pieceSize, pieceSize, BufferedImage.SCALE_SMOOTH);
            c++;
        }

        // Board image
        //BufferedImage image = ImageIO.read(getInputStream(new URL("https://cdn.discordapp.com/attachments/873323229979230258/1005952553919447070/board.png")));
        BufferedImage image = ImageIO.read(App.class.getResource("board.png"));
        Image imagee = (image.getScaledInstance(pieceSize*9, pieceSize*9, BufferedImage.SCALE_SMOOTH));

        // Coords image
        BufferedImage coords = ImageIO.read(App.class.getResource("coords.png"));
        Image coordss = (coords.getScaledInstance((int) (9.5*pieceSize), (int) (9.5*pieceSize), BufferedImage.SCALE_SMOOTH));
        BufferedImage coordsF = ImageIO.read(App.class.getResource("coordsF.png"));
        Image coordssF = (coordsF.getScaledInstance((int) (9.5*pieceSize), (int) (9.5*pieceSize), BufferedImage.SCALE_SMOOTH));

        // GG image
        //BufferedImage gg = ImageIO.read(getInputStream(new URL("https://cdn.discordapp.com/attachments/873323229979230258/1005952554221453433/gg.png")));
        BufferedImage gg = ImageIO.read(App.class.getResource("gg.png"));

        bluePieces = bluePiecesW;
        redPieces = redPiecesW;

        // Drawing the game panel and starting position
        gamePanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (playerID > 1) {
                    bluePieces = redPiecesF;
                    redPieces = bluePiecesF;
                }

                boolean bChec = false;
                boolean rChec = false;
                if (isChecked(true)) {
                    bChec = true;
                    if (lastRedMove.charAt(lastRedMove.length()-1) != '+') lastRedMove += "+";
                } else if (isChecked(false)) {
                    rChec = true;
                    if (lastBlueMove.charAt(lastBlueMove.length()-1) != '+') lastBlueMove += "+";
                }

                g.fillRect(0, 0, (int) (pieceSize * 9.5 * 16/9), (int) (pieceSize * 9.5));
                
                g.setColor(Color.WHITE);
                g.setFont(new Font(Font.SANS_SERIF, 0, 16));
                bCypher.updateMaterial();
                g.drawString(String.valueOf(redMaterial-blueMaterial), 10*pieceSize, pieceSize/2);
                g.drawString(String.valueOf(blueMaterial-redMaterial), 10*pieceSize, 9*pieceSize);
                
                g.setFont(new Font(Font.SANS_SERIF, 0, 40));
                if (lastBlueMove.length() > 4) {
                    g.drawString(lastBlueMove, (int) (10.5*pieceSize), 5*pieceSize);
                } else {
                    g.drawString(lastBlueMove, 11*pieceSize, 5*pieceSize);
                }
                g.drawString(lastRedMove, 14*pieceSize, 5*pieceSize);

                if (playerID == 1) {
                    if (move == 1) {
                        if (getPieceFromPos(2, 6) == null) {
                            opening1 = "Bird Opening";
                        } else if (getPieceFromPos(3, 7) != null) {
                            if (getPieceFromPos(3, 7).type.equalsIgnoreCase("knight")) {
                                opening1 = "Charging Knight Attack";
                            }
                        } else if (getPieceFromPos(4, 6) == null) {
                            opening1 = "Center Game";
                        } else if (getPieceFromPos(5, 6) == null) {
                            opening1 = "English Opening";
                        } else if (getPieceFromPos(7, 6) == null) {
                            opening1 = "Static Yoru Attack";
                        } else if (getPieceFromPos(8, 6) == null) {
                            opening1 = "Russian Opening";
                        } else if (getPieceFromPos(6, 6) == null) {
                            opening1 = "Australian Attack";
                        }
    
    
                    } else if (move == 2) {
                        if (getPieceFromPos(8, 2) == null) {
                            opening2 = "Fianchetto Variation";
                        } else if (getPieceFromPos(1, 2) == null) {
                            opening2 = "Static Yoru Counterattack";
                        } else if (getPieceFromPos(4, 2) == null) {
                            opening2 = "Central System";
                        } else if (getPieceFromPos(6, 2) == null) {
                            opening2 = "Bird Variation";
                        } else if (getPieceFromPos(5, 1) != null) {
                            if (getPieceFromPos(5, 1).type.equalsIgnoreCase("knight")) {
                                if (opening1.equals("Charging Knight Attack")) {
                                    opening2 = "Mirrored Variation";
                                } else {
                                    opening2 = "Charging Knight Attack";
                                }
                            }
                        } else if (getPieceFromPos(5, 2) == null) {
                            opening2 = "Spanish Defense";
                        } else if (getPieceFromPos(7, 2) == null) {
                            opening2 = "Turkish Defense";
                        }
    
                        if (opening1.equals("Bird Opening")) {
                            if (getPieceFromPos(6, 2) == null) {
                                opening2 = "Neon's Gambit";
                            }
    
                        } else if (opening1.equals("English Opening")) {
                            if (getPieceFromPos(2, 2) == null) {
                                opening2 = "London System";
                            }
    
                        } else if (opening1.equals("Center Game")) {
                            if (getPieceFromPos(4, 2) == null) {
                                opening2 = "Mirrored Variation";
                            }
    
                        } else if (opening1.equals("Static Yoru")) {
                            if (getPieceFromPos(7, 6) == null) {
                                opening2 = "Chinese Rally Variation";
                            }
                        
                        } else if (opening1.equals("Russian Opening")) {
                            if (getPieceFromPos(8, 2) == null) {
                                opening2 = "French Defense";
                            } else if (getPieceFromPos(0, 2) == null) {
                                opening2 = "Alaskan Counterattack";
                            }
    
                        } else if (opening1.equals("Australian Attack")) {
                            
                        }
    
    
                    } else if (move == 3) {
                        
                        if (getPieceFromPos(0, 6) == null) {
                            opening3 = "Late Fianchetto";
                        }
    
                        if (opening2.equals("Neon's Gambit")) {
                            if (blueMaterial-redMaterial > 3) {
                                opening3 = "Accepted";
                            } else {
                                if (getPieceFromPos(2, 6) == null && getPieceFromPos(3, 6) != null) {
                                    opening3 = "Swedish Countergambit";
                                } else {
                                    if (getPieceFromPos(2, 6) != null) {
                                        if (getPieceFromPos(2, 6).type.equalsIgnoreCase("bishop")) {
                                            opening3 = "Demon Killer Variation";
                                        } else {
                                            opening3 = "Closed Variation";
                                        }
                                    } else {
                                        opening3 = "Closed Variation";
                                    }
                                }    
                            }
    
                        } else if (opening2.equals("Spanish Defense") && opening1.equals("Bird Opening")) {
                            if (getPieceFromPos(2, 6) != null) {
                                opening3 = "Modern Variation";
                            } else if (getPieceFromPos(2, 5) == null) {
                                opening3 = "Advance Variation";                            
                            } else if (getPieceFromPos(3, 6) == null) {
                                opening3 = "Classical Variation";
                            }
    
                        } else if  (opening2.equals("Bird Variation")) {
                            if (getPieceFromPos(2, 6) == null) {
                                opening3 = "Neon's Gambit";
                            }
                        }
                    }    
                } else {
                    if (move == 1) {
                        if (getPieceFromPos(8-2, 8-6) == null) {
                            opening1 = "Bird Opening";
                        } else if (getPieceFromPos(8-3, 8-7) != null) {
                            if (getPieceFromPos(3, 7).type.equalsIgnoreCase("knight")) {
                                opening1 = "Charging Knight Attack";
                            }
                        } else if (getPieceFromPos(8-4, 8-6) == null) {
                            opening1 = "Center Game";
                        } else if (getPieceFromPos(8-5, 8-6) == null) {
                            opening1 = "English Opening";
                        } else if (getPieceFromPos(8-7, 8-6) == null) {
                            opening1 = "Static Yoru Attack";
                        } else if (getPieceFromPos(8-8, 8-6) == null) {
                            opening1 = "Russian Opening";
                        } else if (getPieceFromPos(8-6, 8-6) == null) {
                            opening1 = "Australian Attack";
                        }
    
    
                    } else if (move == 2) {
                        if (getPieceFromPos(8-8, 8-2) == null) {
                            opening2 = "Fianchetto Variation";
                        } else if (getPieceFromPos(8-1, 8-2) == null) {
                            opening2 = "Static Yoru Counterattack";
                        } else if (getPieceFromPos(8-4, 8-2) == null) {
                            opening2 = "Central System";
                        } else if (getPieceFromPos(8-6, 8-2) == null) {
                            opening2 = "Bird Variation";
                        } else if (getPieceFromPos(8-5, 8-1) != null) {
                            if (getPieceFromPos(8-5, 8-1).type.equalsIgnoreCase("knight")) {
                                if (opening1.equals("Charging Knight Attack")) {
                                    opening2 = "Mirrored Variation";
                                } else {
                                    opening2 = "Charging Knight Attack";
                                }
                            }
                        } else if (getPieceFromPos(8-5, 8-2) == null) {
                            opening2 = "Spanish Defense";
                        } else if (getPieceFromPos(8-7, 8-2) == null) {
                            opening2 = "Turkish Defense";
                        }
    
                        if (opening1.equals("Bird Opening")) {
                            if (getPieceFromPos(8-6, 8-2) == null) {
                                opening2 = "Neon's Gambit";
                            }
    
                        } else if (opening1.equals("English Opening")) {
                            if (getPieceFromPos(8-2, 8-2) == null) {
                                opening2 = "London System";
                            }
    
                        } else if (opening1.equals("Center Game")) {
                            if (getPieceFromPos(8-4, 8-2) == null) {
                                opening2 = "Mirrored Variation";
                            }
    
                        } else if (opening1.equals("Static Yoru")) {
                            if (getPieceFromPos(8-7, 8-6) == null) {
                                opening2 = "Chinese Rally Variation";
                            }
                        
                        } else if (opening1.equals("Russian Opening")) {
                            if (getPieceFromPos(8-8, 8-2) == null) {
                                opening2 = "French Defense";
                            } else if (getPieceFromPos(8-0, 8-2) == null) {
                                opening2 = "Alaskan Counterattack";
                            }
    
                        } else if (opening1.equals("Australian Attack")) {
                            
                        }
    
    
                    } else if (move == 3) {
                        
                        if (getPieceFromPos(8-0, 8-6) == null) {
                            opening3 = "Late Fianchetto";
                        }
    
                        if (opening2.equals("Neon's Gambit")) {
                            if (blueMaterial-redMaterial > 3) {
                                opening3 = "Accepted";
                            } else {
                                if (getPieceFromPos(8-2, 8-6) == null && getPieceFromPos(8-3, 8-6) != null) {
                                    opening3 = "Swedish Countergambit";
                                } else {
                                    if (getPieceFromPos(8-2, 8-6) != null) {
                                        if (getPieceFromPos(8-2, 8-6).type.equalsIgnoreCase("bishop")) {
                                            opening3 = "Demon Killer Variation";
                                        } else {
                                            opening3 = "Closed Variation";
                                        }
                                    } else {
                                        opening3 = "Closed Variation";
                                    }
                                }    
                            }
    
                        } else if (opening2.equals("Spanish Defense") && opening1.equals("Bird Opening")) {
                            if (getPieceFromPos(8-2, 8-6) != null) {
                                opening3 = "Modern Variation";
                            } else if (getPieceFromPos(8-2, 8-5) == null) {
                                opening3 = "Advance Variation";                            
                            } else if (getPieceFromPos(8-3, 8-6) == null) {
                                opening3 = "Classical Variation";
                            }
    
                        } else if  (opening2.equals("Bird Variation")) {
                            if (getPieceFromPos(8-2, 8-6) == null) {
                                opening3 = "Neon's Gambit";
                            }
                        }
                    }    
                }

                g.setFont(new Font("Dialog", 0, 20));
                g.setColor(Color.GRAY);
                g.drawString(opening1, 10*pieceSize, 2*pieceSize);
                g.drawString(opening2, 10*pieceSize, (int) (2.5*pieceSize));
                g.drawString(opening3, 10*pieceSize, 3*pieceSize);

                g.drawImage(imagee, 0, 0, null);
                
                if (playerID > 1) {
                    g.drawImage(coordssF, 0, 0, null);
                } else {
                    g.drawImage(coordss, 0, 0, null);
                }
                

                for (Piece s : stunnedPieces) {
                    g.drawImage(misc[9], s.x, s.y, this);
                }

                if (inBounds(bluePhoenix[0], bluePhoenix[1])) {
                    g.drawImage(misc[8], bluePhoenix[0]*pieceSize, bluePhoenix[1]*pieceSize, this);
                } else if (inBounds(redPhoenix[0], redPhoenix[1])) {
                    g.drawImage(misc[8], redPhoenix[0]*pieceSize, redPhoenix[1]*pieceSize, this);
                }

                if (bChec && selectedPiece != bCypher) {
                    g.drawImage(misc[2], bCypher.x, bCypher.y, this);
                } else if (rChec && selectedPiece != rCypher) {
                    g.drawImage(misc[2], rCypher.x, rCypher.y, this);
                }

                if (blueBoom != null) {
                    int[] xMoves = {0, 0, 1, -1};
                    int[] yMoves = {-1, 1, 0, 0};
                    for (int index = 0; index < xMoves.length; index++) {
                        Piece p = App.getPieceFromPos(blueBoom.xPos+xMoves[index], blueBoom.yPos+yMoves[index]);
                        if (p != null) {
                            if (inBounds(p.xPos, p.yPos) && !p.agent.equalsIgnoreCase("king") && p.isBlue) {
                                g.drawImage(misc[2], p.xPos*pieceSize, p.yPos*pieceSize, this);
                            }
                        }
                    }
                    g.drawImage(misc[2], blueBoom.xPos*pieceSize, blueBoom.yPos*pieceSize, this);
                } else if (redBoom != null) {
                    int[] xMoves = {0, 0, 1, -1};
                    int[] yMoves = {-1, 1, 0, 0};
                    for (int index = 0; index < xMoves.length; index++) {
                        Piece p = App.getPieceFromPos(redBoom.xPos+xMoves[index], redBoom.yPos+yMoves[index]);
                        if (p != null) {
                            if (inBounds(p.xPos, p.yPos) && !p.agent.equalsIgnoreCase("king") && !p.isBlue) {
                                g.drawImage(misc[2], p.xPos*pieceSize, p.yPos*pieceSize, this);
                            }
                        }
                    }
                    g.drawImage(misc[2], redBoom.xPos*pieceSize, redBoom.yPos*pieceSize, this);
                }

                if (selectedPiece != null) {
                    for (int[] move : getAvailableSquares(selectedPiece)) {
                        if ((killedPiece(move[0], move[1], selectedPiece.isBlue) != null) || (killedPiece(move[0], move[1], !selectedPiece.isBlue) != null)) {
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
                    if (p.isStimmed) {
                        if (p.isBlue) {
                            g.drawImage(misc[10], p.x, p.y, this);
                        } else {
                            g.drawImage(misc[11], p.x, p.y, this);
                        }
                    }
                    int i;
                    if (p.agent.startsWith("u")) {
                        i = agents.indexOf(p.agent.substring(1));
                        if (p.isBlue) {
                            g.drawImage(misc[5], p.x, p.y, this);
                        } else {
                            g.drawImage(misc[4], p.x, p.y, this);
                        }
                    } else {
                        i = agents.indexOf(p.agent);
                    }
                    if (p.isBlue && p != selectedPiece) {
                        if (p.agent.equalsIgnoreCase("ureyna") && p.isUlted) {
                            g.drawImage(misc[7], p.x, p.y, this);
                        } else {
                            g.drawImage(bluePieces[i], p.x, p.y, this);
                        }
                    } else if (!p.isBlue && p != selectedPiece) {
                        if (p.agent.equalsIgnoreCase("ureyna") && p.isUlted) {
                            g.drawImage(misc[6], p.x, p.y, this);
                        } else {
                            g.drawImage(redPieces[i], p.x, p.y, this);
                        }
                    }
                    
                }
                for (Piece p: pieces) {
                    List<String> agents = Arrays.asList("raze", "fade", "breach", "reyna", "jett", "phoenix", "skye", "sage", "kayo",
                    "neon", "yoru",
                    "chamber", "omen", "brimstone", "astra", "cypher", "killjoy", "viper", "OMEN", "sova");
                    int i;
                    if (p.agent.startsWith("u")) {
                        i = agents.indexOf(p.agent.substring(1));
                    } else {
                        i = agents.indexOf(p.agent);
                    }
                    if (p.isBlue && p == selectedPiece) {
                        g.drawImage(bluePieces[i], p.x, p.y, this);
                    } else if (!p.isBlue && p == selectedPiece) {
                        g.drawImage(redPieces[i], p.x, p.y, this);
                    }
                }
                if (isCheckmated(App.blue)) g.drawImage(gg.getScaledInstance(pieceSize*9, pieceSize*9, BufferedImage.SCALE_SMOOTH), 0, 0, null);
            }
        };
        BufferedImage icon = ImageIO.read(App.class.getResource("icon.png"));
        setTitle("Rapid Yagura");
        setIconImage(icon);
        add(gamePanel);
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedPiece != null) {
                    selectedPiece.x = e.getX() - pieceSize/2;
                    selectedPiece.y = (int)(e.getY() - pieceSize/1.2);
                    repaint();
                } else {
                    setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
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
                if (!pvp) {
                    if (getPiece(e.getX(), e.getY()) != null && getPiece(e.getX(), e.getY()).isBlue) {
                        selectedPiece = getPiece(e.getX(), e.getY());
                    }
                } else {
                    if (getPiece(e.getX(), e.getY()) != null && getPiece(e.getX(), e.getY()).isBlue && turn) {
                        selectedPiece = getPiece(e.getX(), e.getY());
                    }
                }
                posX = e.getX();
                posY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boolean blu = App.blue;
                int oldX = selectedPiece.xPos;
                int oldY = selectedPiece.yPos;
                int newX = e.getX()/pieceSize;
                int newY = e.getY()/pieceSize;
                selectedPiece.move(newX, newY, false);
                repaint();
                System.out.println(evaluate());
                if (!pvp) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!App.blue) App.redMove();
                            repaint();
                            if(isCheckmated(App.blue)) System.out.println("gg");        
                        }
                    });    
                } else {
                    if (App.blue != blu) {
                        csc.sendMove(8-oldX, 8-oldY, 8-newX, 8-newY);
                    }
                }
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

    private class ClientSideConnection {
        private Socket socket;
        private DataInputStream dis;
        private DataOutputStream dos;

        public ClientSideConnection() {
            try {
                socket = new Socket("localhost", port);
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                playerID = dis.readInt();
                System.out.println("connected as player " + playerID);
                if (playerID == 1) {
                    blue = true;
                    turn = true;
                } else {
                    blue = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMove(int oldX, int oldY, int newX, int newY) {
            try {
                dos.writeInt(oldX);
                dos.writeInt(oldY);
                dos.writeInt(newX);
                dos.writeInt(newY);
                dos.flush();
                turn = !turn;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public int[][] receiveMove() {
            int[][] move = new int[2][2];
            try {
                move[0][0] = dis.readInt();
                move[0][1] = dis.readInt();
                move[1][0] = dis.readInt();
                move[1][1] = dis.readInt();
                System.out.println("Received move: " + move[0][0] + ", " + move[0][1] + " : " + move[1][0] + "," + move[1][1]);
                System.out.println((getPieceFromPos(move[0][0], move[0][1]).isBlue?"blue ":"red ") + getPieceFromPos(move[0][0], move[0][1]).agent);
                getPieceFromPos(move[0][0], move[0][1]).move(move[1][0], move[1][1], false);
                repaint();
                turn = !turn;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return move;
        }
    }

    public void connectToServer() {
        csc = new ClientSideConnection();
    }

    public void startReceivingMoves() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (pvp) csc.receiveMove();
                }
            }
            
        });
        t.start();
    }

    public static DeadPiece getDeadPiece(int x, int y) {
        int xPos = x/pieceSize;
        int yPos = y/pieceSize;
        for(DeadPiece p: deadPieces){
            if(p.xPos == xPos && p.yPos == yPos) {
                return p;
            }
        }
        return null;
    }

    public static DeadPiece getDeadPieceFromPos(int xPos, int yPos, boolean blue) {
        for(DeadPiece p: deadPieces){
            if(p.xPos == xPos && p.yPos == yPos && p.isBlue == blue) {
                return p;
            }
        }
        return null;
    }

    public static Piece getPiece(int x, int y){
        int xPos = x/pieceSize;
        int yPos = y/pieceSize;
        for(Piece p: pieces){
            if(p.xPos == xPos && p.yPos == yPos) {
                return p;
            }
        }
        return null;
    }

    public static Piece getPieceFromPos(int xPos,int yPos){
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
        int stimMod = -1;
        if (piece.isBlue) stimMod = 1;
        // {-1, -1,  0, +1, +1}
        // {-1*stimMod, +1*stimMod, -1*stimMod, +1*stimMod, -1*stimMod}
        if (piece.getType().equalsIgnoreCase("knight")) {
            if (piece.isStimmed) {
                int[] xMoves = {-2, -2, -1, +1, +2, +2, +1, -1, -1, -1,  0, +1, +1};
                int[] yMoves = {-1, +1, +2, +2, +1, -1, -2, -2, -1*stimMod, +1*stimMod, -1*stimMod, +1*stimMod, -1*stimMod};
                return(new int[][]{xMoves, yMoves});    
            } else {
                int[] xMoves = {-2, -2, -1, +1, +2, +2, +1, -1};
                int[] yMoves = {-1, +1, +2, +2, +1, -1, -2, -2};
                return(new int[][]{xMoves, yMoves});    
            }
        } else if (piece.getType().equalsIgnoreCase("pawn")) {
            if (piece.isStimmed) {
                int[] xMoves = {-1, -1,  0, +1, +1};
                int[] yMoves = {-1*stimMod, +1*stimMod, -1*stimMod, +1*stimMod, -1*stimMod};
                return(new int[][]{xMoves, yMoves});
            } else {

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
            }
        } else if (piece.getType().equalsIgnoreCase("lance")) {
            int mod = 1;
            if (piece.isBlue) mod = -1;
            int x;
            int y;
            ArrayList<Integer> xMoveList = new ArrayList<Integer>();
            ArrayList<Integer> yMoveList = new ArrayList<Integer>();
            // {-1, -1,  0, +1, +1}
            // {-1*stimMod, +1*stimMod, -1*stimMod, +1*stimMod, -1*stimMod}
            if (piece.isStimmed) {
                xMoveList.add(-1);
                yMoveList.add(-1*stimMod);
                xMoveList.add(-1);
                yMoveList.add(1*stimMod);
                xMoveList.add(0);
                yMoveList.add(-1*stimMod);
                xMoveList.add(1);
                yMoveList.add(1*stimMod);
                xMoveList.add(1);
                yMoveList.add(-1*stimMod);    
            }

            xMoveList.add(-1);
            xMoveList.add(1);
            yMoveList.add(0);
            yMoveList.add(0);
            x = piece.xPos;
            y = piece.yPos + mod;
            while (y < 9 && y >= 0) {
                if (getPiece(x*pieceSize, y*pieceSize) == null) {
                    y += mod;
                    continue;
                } else if (getPiece(x*pieceSize, y*pieceSize).isBlue != piece.isBlue) {
                    yMoveList.add(y-piece.yPos);
                    xMoveList.add(0);
                    break;
                } else {
                    break;
                }
            }

            int[] xMoves = new int[xMoveList.size()];
            int[] yMoves = new int[yMoveList.size()];
            for (int i = 0; i < xMoveList.size(); i++) {
                xMoves[i] = xMoveList.get(i);
            }
            for (int i = 0; i < yMoveList.size(); i++) {
                yMoves[i] = yMoveList.get(i);
            }
            return(new int[][]{xMoves, yMoves});
        } else if (piece.getType().equalsIgnoreCase("king")) {
            int[] xMoves = {-1, -1, -1,  0,  0, +1, +1, +1, +1, +1};
            int[] yMoves = {-1,  0, +1, +1, -1, -1,  0, +1,  0,  0};
            boolean blue = piece.isBlue;
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
        } else if (piece.getType().equalsIgnoreCase("rook")) {
            int x;
            int y;
            ArrayList<Integer> xMoveList = new ArrayList<Integer>();
            ArrayList<Integer> yMoveList = new ArrayList<Integer>();
            if (piece.isUlted || piece.isStimmed) {
                xMoveList.add(-1);
                yMoveList.add(-1);
                xMoveList.add(-1);
                yMoveList.add(+1);
                xMoveList.add(+1);
                yMoveList.add(-1);
                xMoveList.add(+1);
                yMoveList.add(+1);
            }
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
            int x;
            int y;
            ArrayList<Integer> xMoveList = new ArrayList<Integer>();
            ArrayList<Integer> yMoveList = new ArrayList<Integer>();
            if (piece.isUlted) {
                xMoveList.add(-1);
                yMoveList.add(0);
                xMoveList.add(+1);
                yMoveList.add(0);
                xMoveList.add(0);
                yMoveList.add(-1);
                xMoveList.add(0);
                yMoveList.add(+1);
            } else if (piece.isStimmed) {
                xMoveList.add(0);
                yMoveList.add(-1*stimMod);
            }
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
        ArrayList<int[]> availableSquares = new ArrayList<int[]>();
        if (!inBounds(piece.xPos, piece.yPos)) return availableSquares;
        if (piece.isStunned) return availableSquares;
        // {-1, -1,  0, +1, +1}
        // {-1, +1, -1, +1, -1}
        int[][] moves = getRawMoves(piece);
        int[] xMoves = moves[0];
        int[] yMoves = moves[1];
        if (piece.agent.equalsIgnoreCase("uomen") && piece.isUlted) {
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (getPieceFromPos(c, r) == null) {
                        availableSquares.add(new int[]{c, r});
                    }
                }
            }
        }
        for (int i = 0; i < yMoves.length; i++) {
            int x = piece.xPos;
            int y = piece.yPos;
            x += xMoves[i];
            y += yMoves[i];
            if (!piece.agent.equalsIgnoreCase("brimstone") && occupied(piece.isBlue, x, y)) {
                continue;
            }
            if (!inBounds(x, y)) {
                continue;
            }
            boolean ded = false;
            boolean fed = false;
            int oldx = -1;
            int oldy = -1;
            if (killedPiece(x, y, piece.isBlue) != null) {
                if (killedPiece(x, y, piece.isBlue).agent.equalsIgnoreCase("ureyna") && killedPiece(x, y, piece.isBlue).isUlted) {
                    continue;
                } else if (killedPiece(x, y, piece.isBlue).agent.equalsIgnoreCase("uphoenix")) {
                    boolean yes = false;
                    if (piece.isBlue && inBounds(redPhoenix[0], redPhoenix[1])) yes = true;
                    if (!piece.isBlue && inBounds(bluePhoenix[0], bluePhoenix[1])) yes = true;
                    if (yes) {
                        oldx = killedPiece(x, y, piece.isBlue).xPos;
                        oldy = killedPiece(x, y, piece.isBlue).yPos;
                        if (piece.isBlue) {
                            if (x == redPhoenix[0] && y == redPhoenix[1]) continue;
                            fed = true;
                            killedPiece(x, y, piece.isBlue).move(redPhoenix[0], redPhoenix[1], true);
                        } else {
                            if (x == bluePhoenix[0] && y == bluePhoenix[1]) continue;
                            fed = true;
                            killedPiece(x, y, piece.isBlue).move(bluePhoenix[0], bluePhoenix[1], true);
                        }
                    }
                } else {
                    ded = true;
                    oldx = killedPiece(x, y, piece.isBlue).xPos;
                    oldy = killedPiece(x, y, piece.isBlue).yPos;
                    killedPiece(x, y, piece.isBlue).move(10, 10, true);
                } 
            }
            piece.move(x, y, true);
            if (!isChecked(piece.isBlue)) {
                if (!availableSquares.contains(new int[]{x, y})) availableSquares.add(new int[]{x, y});
            }
            piece.move(x-xMoves[i], y-yMoves[i], true);
            int ex = -1, why = -1;
            if (ded) {
                ex = 10*pieceSize;
                why = 10*pieceSize;
            } else if (fed) {
                if (piece.isBlue) {
                    ex = redPhoenix[0]*pieceSize;
                    why = redPhoenix[1]*pieceSize;
                } else {
                    ex = bluePhoenix[0]*pieceSize;
                    why = bluePhoenix[1]*pieceSize;
                }
            }
            if (ded || fed) getPiece(ex, why).move(oldx, oldy, true);
            
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

    public static boolean silverCheck(boolean blue) {
        Piece p = pieces.stream().filter((s) -> (s.isBlue == blue) && s != null && s.type.equalsIgnoreCase("king")).findFirst().orElse(null);
        int yMod;
        if (blue) yMod = -1;
        else yMod = 1;
        int x = p.xPos;
        int y = p.yPos;
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
            if (getPiece((x+silverX[i])*pieceSize, (y+silverY[i])*pieceSize) != null && (getPiece((x+silverX[i])*pieceSize, (y+silverY[i])*pieceSize).getType().equalsIgnoreCase("silver") || getPiece((x+silverX[i])*pieceSize, (y+silverY[i])*pieceSize).isStimmed) && getPiece((x+silverX[i])*pieceSize, (y+silverY[i])*pieceSize).isBlue != blue) {
                return(true);
            }
        }
        return false;

    }

    public static boolean isChecked(boolean blue) {
        int yMod;
        if (blue) yMod = -1;
        else yMod = 1;
        // Find the king on the board
        Piece p = pieces.stream().filter((s) -> (s.isBlue == blue) && s != null && s.type.equalsIgnoreCase("king")).findFirst().orElse(null);
        // If the king is checked by a lance
        if (getPieceFromPos(p.xPos+1, p.yPos) != null) {
            if (getPieceFromPos(p.xPos+1, p.yPos).getType().equalsIgnoreCase("lance") && getPieceFromPos(p.xPos+1, p.yPos).isBlue != blue) return true;
        } else if (getPieceFromPos(p.xPos-1, p.yPos) != null) {
            if (getPieceFromPos(p.xPos-1, p.yPos).getType().equalsIgnoreCase("lance") && getPieceFromPos(p.xPos-1, p.yPos).isBlue != blue) return true;
        }

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
            else if (getPiece((x+knightX[i])*pieceSize, (y+knightY[i])*pieceSize).getType().equalsIgnoreCase("knight") && getPiece((x+knightX[i])*pieceSize, (y+knightY[i])*pieceSize).isBlue != blue) {
                return(true);
            }
        }
        // If the king is checked by a silver
        if (silverCheck(blue)) return true;

        // If the king is checked by a king
        x = p.xPos;
        y = p.yPos;
        int[] kingX = {-1, -1, -1,  0,  0, +1, +1, +1};
        int[] kingY = {-1,  0, +1, +1, -1, -1,  0, +1};
        for (int i = 0; i < kingX.length; i++) {
            if (getPieceFromPos(x+kingX[i], y+kingY[i]) == null) continue;
            if (getPieceFromPos(x+kingX[i], y+kingY[i]) != null && (getPieceFromPos(x+kingX[i], y+kingY[i]).type.equalsIgnoreCase("king") || ((getPieceFromPos(x+kingX[i], y+kingY[i]).type.equalsIgnoreCase("bishop")) || getPieceFromPos(x+kingX[i], y+kingY[i]).type.equalsIgnoreCase("rook")) && (getPieceFromPos(x+kingX[i], y+kingY[i]).isUlted)) && getPieceFromPos(x+kingX[i], y+kingY[i]).isBlue != blue) {
                return true;
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
        }
        if (c.yPos != rank || c.xPos != file) {
            castle = false;
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

    public static boolean isStalemated(boolean blue) {
        if (isChecked(blue)) {
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

    public static float evaluate() {
        float evaluation = 0;
        evaluation += (blueMaterial - redMaterial);
        evaluation += (blueVision/10 - redVision/10);
        int[] xMoves = {0, 0, 1, -1};
        int[] yMoves = {-1, 1, 0, 0};
        if (blueBoom != null) {
            for (int index = 0; index < xMoves.length; index++) {
                Piece p = App.getPieceFromPos(blueBoom.xPos+xMoves[index], blueBoom.yPos+yMoves[index]);
                if (p != null) {
                    evaluation -= p.value;
                }
            }
        } else if (redBoom != null) {
            for (int index = 0; index < xMoves.length; index++) {
                Piece p = App.getPieceFromPos(redBoom.xPos+xMoves[index], redBoom.yPos+yMoves[index]);
                if (p != null) {
                    evaluation += p.value;
                }
            }
        }

        return evaluation;
    }

    public static float search(int depth, float alpha, float beta, boolean blue) {
        if (depth == 0) return evaluate();
        if (isCheckmated(true)) {
            System.out.println("L");
            return Float.NEGATIVE_INFINITY;
        }
        for (int i = 0; i < pieces.size(); i++) {
            Piece p = pieces.get(i);
            if (p.isBlue == blue) {
                int xPos = p.xPos;
                int yPos = p.yPos;
                for (int[] move : getAvailableSquares(p)) {
                    DeadPiece d = null;
                    if (p.type.equalsIgnoreCase("bishop")) System.out.println(p.xPos + " " + p.agent + " " + move[0] + "," + move[1]);
                    if (p.type.equalsIgnoreCase("king") && Math.abs(move[0] - xPos) > 1) {
                        p.move(move[0], move[1], false);
                    } else {
                        p.move(move[0], move[1], true);
                    }
                    if (killedPiece(move[0], move[1], blue) != null) {
                        Piece k = killedPiece(move[0], move[1], blue);
                        d = new DeadPiece(k.agent, move[0], move[1], k.isBlue, deadPieces);
                        killedPiece(move[0], move[1], blue).move(move[0]+(10*(depth+1)), move[1]+(10*(depth+1)), true);
                    }
                    if ((p.type.equalsIgnoreCase("lance") && move[0] - xPos == 0) || (p.agent.equalsIgnoreCase("uraze") && d != null)) {
                        p.move(xPos, yPos, true);
                    }

                    p.updateVision();
                    p.updateMaterial();
                    if ((Math.abs(4-move[0]) <= Math.abs(4-xPos)) && (Math.abs(4-move[1]) <= Math.abs(4-yPos))) {
                        if (blue) {
                            blueVision++;
                        } else {
                            redVision++;
                        }
                    }
                    float evaluation = -search(depth - 1, -beta, -alpha, !blue);
                    if (p.type.equalsIgnoreCase("king") && (move[0] - xPos) > 1) {
                        getPieceFromPos(xPos+1, yPos).move(8, yPos, true);
                        getPieceFromPos(8, yPos).updateXY();
                        if (blue) blueCastled = false;
                        else redCastled = false;
                    } else if (p.type.equalsIgnoreCase("king") && (move[0] - xPos) < -1) {
                        getPieceFromPos(xPos-1, yPos).move(0, yPos, true);
                        getPieceFromPos(0, yPos).updateXY();
                        if (blue) blueCastled = false;
                        else redCastled = false;
                    }
                    if (!(p.type.equalsIgnoreCase("lance") && move[0] - xPos == 0)) {
                        if (p.type.equalsIgnoreCase("bishop")) System.out.println(p.agent + " back to " + xPos + "," + yPos);
                        p.move(xPos, yPos, true);
                        p.updateXY();
                    }
                    if (d != null) {
                        d.kill();
                        getPieceFromPos((move[0]+(10*(depth+1))), (move[1]+(10*(depth+1)))).move(move[0], move[1], true);
                    }
                    if (evaluation >= beta) {
                        return beta;
                    }
                    if (evaluation > alpha) {
                        alpha = evaluation;
                    }
                }
            }
        }
        return alpha;
    }

    public static void redMove() {
        search = true;
        boolean bestCapture = false;
        int[] bestOrigin = {0, 0};
        int[] bestMove = {10,10};
        float bestCurrentEval = Float.POSITIVE_INFINITY;
        float bestEval = Float.POSITIVE_INFINITY;
        for (int i = 0; i < pieces.size(); i++) {
            Piece p = pieces.get(i);
            if (!p.isBlue) {
                for (int[] move : getAvailableSquares(p)) {
                    System.out.println(move[0] + "," + move[1]);
                    boolean capture = false;
                    int xPos = p.xPos;
                    int yPos = p.yPos;
                    if (p.type.equalsIgnoreCase("king") && Math.abs(move[0] - xPos) > 1) {
                        p.move(move[0], move[1], false);
                    } else {
                        p.move(move[0], move[1], true);
                    }
                    if (killedPiece(move[0], move[1], p.isBlue) != null) {
                        killedPiece(move[0], move[1], p.isBlue).move(-10, -10, true);
                        capture = true;
                    }
                    if (p.type.equalsIgnoreCase("lance") && move[0] - xPos == 0) {
                        p.move(xPos, yPos, true);
                    }
                    p.updateVision();
                    p.updateMaterial();
                    if ((Math.abs(4-move[0]) <= Math.abs(4-xPos)) && (Math.abs(4-move[1]) <= Math.abs(4-yPos))) {
                        App.redVision++;
                    }
                    float eval = search(1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, true);
                    if (eval < bestEval) {
                        bestCurrentEval = evaluate();
                        bestOrigin[0] = xPos*pieceSize;
                        bestOrigin[1] = yPos*pieceSize;
                        bestEval = eval;
                        bestMove[0] = move[0];
                        bestMove[1] = move[1];
                        bestCapture = capture;
                    } else if (eval == bestEval) {
                        if (evaluate() < bestCurrentEval) {
                            bestCurrentEval = evaluate();
                            bestOrigin[0] = xPos*pieceSize;
                            bestOrigin[1] = yPos*pieceSize;
                            bestEval = eval;
                            bestMove[0] = move[0];
                            bestMove[1] = move[1];
                            bestCapture = capture;
                        }
                    }
                    if (p.type.equalsIgnoreCase("king") && (move[0] - xPos) > 1) {
                        getPieceFromPos(xPos+1, yPos).move(8, yPos, true);
                        getPieceFromPos(8, yPos).updateXY();
                        redCastled = false;
                    } else if (p.type.equalsIgnoreCase("king") && (move[0] - xPos) < -1) {
                        getPieceFromPos(xPos-1, yPos).move(0, yPos, true);
                        getPieceFromPos(0, yPos).updateXY();
                        redCastled = false;
                    }
                    if (!(p.type.equalsIgnoreCase("lance") && move[0] - xPos == 0)) {
                        lastDestination[0] = move[0];
                        lastDestination[1] = move[1];
                        p.move(xPos, yPos, true);
                        p.updateXY();
                    }

                    if (getPiece(pieceSize*(-10), pieceSize*(-10)) != null) {
                        getPiece(pieceSize*(-10), pieceSize*(-10)).move(move[0], move[1], true);
                    }
                }
            }
        }
        search = false;
        System.out.println("moved " + getPiece(bestOrigin[0], bestOrigin[1]).agent + " to " + bestMove[0] + "," + bestMove[1]);
        if (getPiece(bestOrigin[0], bestOrigin[1]).type.equalsIgnoreCase("king") && Math.abs(bestMove[0] - bestOrigin[0]/pieceSize) > 1) {
            getPiece(bestOrigin[0], bestOrigin[1]).move(bestMove[0], bestMove[1], true);
            if ((bestMove[0] - bestOrigin[0]/pieceSize) > 1) {
                getPiece(8*pieceSize, bestOrigin[1]).move(bestOrigin[0]/pieceSize+1, bestOrigin[1]/pieceSize, true);
                getPiece(bestOrigin[0]+pieceSize, bestOrigin[1]).updateXY();
            } else {
                getPiece(0, bestOrigin[1]).move(bestOrigin[0]/pieceSize-1, bestOrigin[1]/pieceSize, true);
                getPiece(bestOrigin[0]-pieceSize, bestOrigin[1]).updateXY();
            }
        } else {
            getPiece(bestOrigin[0], bestOrigin[1]).move(bestMove[0], bestMove[1], false);
        }
        if (getPieceFromPos(bestMove[0], bestMove[1]) != null) {
            getPieceFromPos(bestMove[0], bestMove[1]).updateXY();
        }
        if (bestCapture) {
            try {
                playSound(captureURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                playSound(moveURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        blue = true;
    }

    public static void playSound(URL url) {
        AudioInputStream InputStream;
        try {
            InputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(InputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        App a = new App(9999);
        if (!pvp) {
            a.connectToServer();
            a.startReceivingMoves();
        }
    }
}
