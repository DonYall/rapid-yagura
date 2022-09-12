import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class MenuAdv extends JFrame {
    JPanel panel;
    JPanel panel2;
    JPanel panel3;
    JLabel label;
    JTextField textField;
    JButton button;
    JLabel label2;
    JButton button2;
    JLabel ip;
    JButton adv;

    public MenuAdv() throws IOException {
        panel = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        label = new JLabel("Start a server");
        textField = new JTextField("3 digit game code", 10);
        button = new JButton("Start");
        label2 = new JLabel("Join an official server");
        button2 = new JButton("See servers");
        adv = new JButton("Play Local");
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
                    new ServerStarted(InetAddress.getLocalHost().getHostAddress(), port);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                dispose();
            }      
        });
        panel2.add(label2);
        panel2.add(button2);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ServerList();
                dispose();
            }      
        });
        panel3.add(ip);
        panel3.add(adv);
        adv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new Menu();
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
        setTitle("Play Online");
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

    public String decryptIP(String ip) {
        String newIP = "";
        for (int i = 0; i < ip.length(); i++) {
            if (Character.hashCode(ip.charAt(i)) - 97 >= 0) {
                newIP += Character.hashCode(ip.charAt(i)) - 97;
            } else {
                newIP += ".";
            }
        }
        return newIP;
    }

    private class ServerStarted extends JFrame {
        JPanel panel;
        JLabel label;
        JLabel label2;

        public ServerStarted(String ip, int port) {
            panel = new JPanel();
            label = new JLabel("Server ID: " + encryptIP(ip));
            label2 = new JLabel("Game Code: " + port);
            panel.add(label);
            panel.add(label2);
            panel.setBackground(Color.GRAY.darker());
            label.setBackground(Color.GRAY.darker());
            label.setForeground(Color.WHITE);
            label2.setBackground(Color.GRAY.darker());
            label2.setForeground(Color.WHITE);
            add(panel);
            try {
                BufferedImage icon = ImageIO.read(App.class.getResource("icon.png"));
                setIconImage(icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
            setTitle("Server started");
            setSize(200, 100);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
    }

    private class ServerList extends JFrame {
        JPanel portPanel;
        JPanel playersPanel;
        JPanel joinPanel;
        JLabel portHeader;
        JLabel playersHeader;
        JLabel joinHeader;
        JLabel[] portLabels = new JLabel[10];
        JLabel[] playersLabels = new JLabel[10];
        JButton[] joinButtons = new JButton[10];
        JButton refreshButton;
        
        public ServerList() {
            portPanel = new JPanel();
            playersPanel = new JPanel();
            joinPanel = new JPanel();
            portHeader = new JLabel("Code", JLabel.CENTER);
            portHeader.setBackground(Color.GRAY.darker());
            portHeader.setForeground(Color.CYAN);
            playersHeader = new JLabel("Players", JLabel.CENTER);
            playersHeader.setBackground(Color.GRAY.darker());
            playersHeader.setForeground(Color.CYAN);
            joinHeader = new JLabel("Join", JLabel.CENTER);
            joinHeader.setBackground(Color.GRAY.darker());
            joinHeader.setForeground(Color.CYAN);
            portPanel.add(portHeader);
            playersPanel.add(playersHeader);
            joinPanel.add(joinHeader);
            for (int i = 0; i < 10; i++) {
                portLabels[i] = new JLabel("" + (i + 420), JLabel.CENTER);
                portLabels[i].setForeground(Color.WHITE);
                try {
                    int timeout = (int)TimeUnit.MILLISECONDS.toMillis(100);
                    Socket socket1 = new Socket();//(decryptIP("jh-bai-bej-bd"), i + 420);
                    socket1.connect(new InetSocketAddress(decryptIP("jh-bai-bej-bd"), i + 420), timeout);
                    DataInputStream dis1 = new DataInputStream(socket1.getInputStream());
                    DataOutputStream dos1 = new DataOutputStream(socket1.getOutputStream());
                    dos1.writeInt(-1);
                    dos1.flush();
                    int status = -2;
                    while (status < -1) {
                        status = dis1.readInt();
                    }
                    playersLabels[i] = new JLabel("" + status, JLabel.CENTER);
                    playersLabels[i].setForeground(Color.GREEN.brighter());
                    socket1.close();
                } catch (Exception e) {
                    playersLabels[i] = new JLabel("Down", JLabel.CENTER);
                    playersLabels[i].setForeground(Color.PINK);
                }
                //playersLabels[i] = new JLabel("", JLabel.CENTER);
                //playersLabels[i].setForeground(Color.WHITE);
                joinButtons[i] = new JButton("Join");
                if (playersLabels[i].getText().equals("Down")) joinButtons[i].setEnabled(false);
                joinButtons[i].setBackground(Color.GRAY);
                joinButtons[i].setForeground(Color.WHITE);
            }
            refreshButton = new JButton("Refresh");
            refreshButton.setBackground(Color.GRAY);
            refreshButton.setForeground(Color.WHITE);

            portPanel.setLayout(new GridLayout(12, 1));
            playersPanel.setLayout(new GridLayout(12, 1));
            joinPanel.setLayout(new GridLayout(12, 1));
            for (int i = 0; i < 10; i++) {
                portPanel.add(portLabels[i]);
                playersPanel.add(playersLabels[i]);
                joinPanel.add(joinButtons[i]);
            }
            for (int i = 0; i < 10; i++) {
                int j = i;
                joinButtons[i].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int port = Integer.parseInt(portLabels[j].getText());
                            App app = new App("don", port);
                            app.connectToServer();
                            app.startReceivingMoves();
                            dispose();            
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        dispose();
                    }      
                });
            }
            portPanel.add(refreshButton);
            refreshButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < 10; i++) {
                        try {
                            int timeout = (int)TimeUnit.MILLISECONDS.toMillis(100);
                            Socket socket1 = new Socket();//(decryptIP("jh-bai-bej-bd"), i + 420);
                            socket1.connect(new InetSocketAddress(decryptIP("jh-bai-bej-bd"), i + 420), timeout);
                            DataInputStream dis1 = new DataInputStream(socket1.getInputStream());
                            DataOutputStream dos1 = new DataOutputStream(socket1.getOutputStream());
                            dos1.writeInt(-1);
                            dos1.flush();
                            int status = -2;
                            while (status < -1) {
                                status = dis1.readInt();
                            }
                            playersLabels[i].setText("" + status);
                            playersLabels[i].setForeground(Color.GREEN.brighter());
                            socket1.close();
                        } catch (Exception e1) {
                            playersLabels[i].setText("Down");
                            playersLabels[i].setForeground(Color.PINK);
                        }
                        if (playersLabels[i].getText().equals("Down")) {
                            joinButtons[i].setEnabled(false);
                        } else joinButtons[i].setEnabled(true);        
                    }
                    repaint();
                }
                
            });
            portPanel.setBackground(Color.GRAY.darker());
            playersPanel.setBackground(Color.GRAY.darker());
            joinPanel.setBackground(Color.GRAY.darker());
            add(portPanel, BorderLayout.WEST);
            add(playersPanel, BorderLayout.CENTER);
            add(joinPanel, BorderLayout.EAST);
            try {
                BufferedImage icon = ImageIO.read(App.class.getResource("icon.png"));
                setIconImage(icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
            setTitle("Server List");
            setSize(350, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
    }
    
    public static void main(String[] args) throws IOException {
        new MenuAdv();
    }
}
