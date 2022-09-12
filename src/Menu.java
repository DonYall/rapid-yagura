import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Menu extends JFrame {
    JPanel panel;
    JPanel panel2;
    JPanel panel3;
    JLabel label;
    JTextField textField;
    JButton button;
    JLabel label2;
    JTextField textField2;
    JTextField textField3;
    JButton button2;
    JLabel ip;
    JButton adv;

    public Menu() throws IOException {
        panel = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        label = new JLabel("Host a local game");
        textField = new JTextField("3 digit game code", 10);
        button = new JButton("Start");
        label2 = new JLabel("Join a local game");
        textField2 = new JTextField("3 digit game code", 10);
        textField3 = new JTextField("Server ID", 10);
        button2 = new JButton("Join");
        adv = new JButton("Play Online");
        ip = new JLabel("Your Server ID: " + encryptIP(InetAddress.getLocalHost().getHostAddress()));
        label.setBackground(Color.GRAY.darker());
        label.setForeground(Color.CYAN);
        textField.setBackground(Color.GRAY);
        textField.setForeground(Color.WHITE);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBorder(null);
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        label2.setBackground(Color.GRAY.darker());
        label2.setForeground(Color.CYAN);
        textField2.setBackground(Color.GRAY);
        textField2.setForeground(Color.WHITE);
        textField2.setHorizontalAlignment(JTextField.CENTER);
        textField2.setBorder(null);
        textField3.setBackground(Color.GRAY);
        textField3.setForeground(Color.WHITE);
        textField3.setHorizontalAlignment(JTextField.CENTER);
        textField3.setBorder(null);
        button2.setBackground(Color.GRAY);
        button2.setForeground(Color.WHITE);
        ip.setBackground(Color.GRAY.darker());
        ip.setForeground(Color.WHITE);
        adv.setBackground(Color.GRAY.darker());
        adv.setForeground(Color.WHITE);
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
                    App app = new App(encryptIP(InetAddress.getLocalHost().getHostAddress()), port);
                    app.connectToServer();
                    app.startReceivingMoves();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                dispose();
            }      
        });
        panel2.add(label2);
        panel2.add(textField2);
        panel2.add(textField3);
        panel2.add(button2);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(textField2.getText());
                try {
                    App app = new App(textField3.getText(), port);
                    app.connectToServer();
                    app.startReceivingMoves();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                dispose();
            }      
        });
        panel3.add(ip);
        panel3.add(adv);
        adv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new MenuAdv();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dispose();
            }      
        });
        panel.setBackground(Color.GRAY.darker());
        panel2.setBackground(Color.GRAY.darker());
        panel3.setBackground(Color.GRAY.darker());
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);
        setTitle("Play on the same WiFi network");
        BufferedImage icon = ImageIO.read(App.class.getResource("icon.png"));
        setIconImage(icon);
        setSize(600, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public String encryptIP(String ip) {
        String newIP = "";
        for (int i = 0; i < ip.length(); i++) {
            try {
                newIP += (char)(Integer.parseInt(Character.toString(ip.charAt(i))) + 97);
            } catch (Exception e) {
                newIP += "-";
            }
        }
        return newIP;
    }
    
    public static void main(String[] args) throws IOException {
        new Menu();
    }
}
