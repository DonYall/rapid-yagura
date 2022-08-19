import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Menu extends JFrame {
    JPanel panel;
    JLabel label;
    JTextField textField;
    JButton button;
    JLabel label2;
    JTextField textField2;
    JButton button2;
    JLabel error;

    public Menu() throws IOException {
        panel = new JPanel();
        label = new JLabel("Host a game");
        textField = new JTextField("4-digit game code");
        button = new JButton("Start");
        label2 = new JLabel("Join a game");
        textField2 = new JTextField("4-digit game code");
        button2 = new JButton("Join");
        error = new JLabel();
        panel.add(label);
        panel.add(textField);
        panel.add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(textField.getText());
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        GameServer gs = new GameServer(port);
                        gs.acceptConnections();
                    }
                });
                t.start();
                try {
                    App app = new App(port);
                    app.connectToServer();
                    app.startReceivingMoves();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                dispose();
            }      
        });
        panel.add(label2);
        panel.add(textField2);
        panel.add(button2);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(textField2.getText());
                try {
                    App app = new App(port);
                    app.connectToServer();
                    app.startReceivingMoves();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                dispose();
            }      
        });
        setLayout(new FlowLayout());
        add(panel);
        setTitle("Main Menu");
        BufferedImage icon = ImageIO.read(App.class.getResource("icon.png"));
        setIconImage(icon);
        setSize(600, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public static void main(String[] args) throws IOException {
        new Menu();
    }
}
