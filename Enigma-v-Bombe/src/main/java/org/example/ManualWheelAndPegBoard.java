package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;


// Below this is GUI
public class ManualWheelAndPegBoard extends JPanel implements MouseListener {
    private final Map<Character, Point> pegPositions = new HashMap<>();
    private final Map<Character, Character> pegPairs = new HashMap<>();
    private final Map<Character, Color> pegColors = new HashMap<>();
    private final List<Color> colorPool = new ArrayList<>();
    private Character firstClicked = null;


    private boolean okPressed = false;
    private JFrame parentFrame;



    private int wheel1 = 1;
    private int wheel2 = 1;
    private int wheel3 = 1;

    private int[] encryptionArray;

    public ManualWheelAndPegBoard(int[] encryptionArray, JFrame frame) {
        this.encryptionArray = encryptionArray;
        this.parentFrame = frame;
        this.wheel1 = encryptionArray[0];
        this.wheel2 = encryptionArray[1];
        this.wheel3 = encryptionArray[2];

        this.setPreferredSize(new Dimension(600, 700));
        this.setLayout(null); // For absolute positioning
        this.setFocusable(true);
        this.addMouseListener(this);

        initColors();
        setupButtons();
    }

    private void setupButtons() {
        // Wheel 1 Buttons
        JButton up1 = new JButton("↑");
        JButton down1 = new JButton("↓");
        up1.setBounds(120, 320, 50, 30);
        down1.setBounds(120, 360, 50, 30);
        this.add(up1);
        this.add(down1);

        up1.addActionListener(e -> {
            wheel1 = wheel1 == 26 ? 1 : wheel1 + 1;
            repaint();
            //encryptionarray[0] = wheel1;
        });
        down1.addActionListener(e -> {
            wheel1 = wheel1 == 1 ? 26 : wheel1 - 1;
            repaint();
           // encryptionarray[0] = wheel1;
        });

        // Wheel 2 Buttons
        JButton up2 = new JButton("↑");
        JButton down2 = new JButton("↓");
        up2.setBounds(270, 320, 50, 30);
        down2.setBounds(270, 360, 50, 30);
        this.add(up2);
        this.add(down2);

        up2.addActionListener(e -> {
            wheel2 = wheel2 == 26 ? 1 : wheel2 + 1;
            repaint();
           // encryptionarray[1] = wheel2;
        });
        down2.addActionListener(e -> {
            wheel2 = wheel2 == 1 ? 26 : wheel2 - 1;
            repaint();
          //  encryptionarray[1] = wheel2;
        });

        // Wheel 3 Buttons
        JButton up3 = new JButton("↑");
        JButton down3 = new JButton("↓");
        up3.setBounds(420, 320, 50, 30);
        down3.setBounds(420, 360, 50, 30);
        this.add(up3);
        this.add(down3);

        up3.addActionListener(e -> {
            wheel3 = wheel3 == 26 ? 1 : wheel3 + 1;
            repaint();
          //  encryptionarray[2] = wheel3;
        });
        down3.addActionListener(e -> {
            wheel3 = wheel3 == 1 ? 26 : wheel3 - 1;
            repaint();
          //  encryptionarray[2] = wheel3;
        });
 // OK BUTTON FUNCTIONALITY
        JButton okButton = new JButton("OK");
        okButton.setBounds(250, 420, 100, 40);
        this.add(okButton);

        okButton.addActionListener(e -> {
          //  JOptionPane.showMessageDialog(this,
            //        String.format("Submitted:\nWheel 1: %d\nWheel 2: %d\nWheel 3: %d", wheel1, wheel2, wheel3), "Wheels Submitted", JOptionPane.INFORMATION_MESSAGE);
            //return encryptionarray;
            encryptionArray[0] = wheel1;
            encryptionArray[1] = wheel2;
            encryptionArray[2] = wheel3;

            okPressed = true;

            JOptionPane.showMessageDialog(this,
                    String.format("Submitted:\nWheel 1: %d\nWheel 2: %d\nWheel 3: %d", wheel1, wheel2, wheel3),
                    "Wheels Submitted", JOptionPane.INFORMATION_MESSAGE);

            // Close the window
            if (parentFrame != null) {
                parentFrame.dispose();
            }
        });
    }
    public boolean isOkPressed() {
        return okPressed;
    }
    public int[] getWheelValues() {
        return new int[]{wheel1, wheel2, wheel3};
    }
    private void initColors() {
        colorPool.add(Color.RED);
        colorPool.add(Color.BLUE);
        colorPool.add(Color.GREEN);
        colorPool.add(Color.MAGENTA);
        colorPool.add(Color.CYAN);
        colorPool.add(Color.YELLOW);
        colorPool.add(Color.PINK);
        colorPool.add(new Color(255, 165, 0)); // Orange
        colorPool.add(new Color(128, 0, 128)); // Purple
        colorPool.add(new Color(0, 128, 128)); // Teal
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        // Background
        Color brown = new Color(139, 69, 19); // SaddleBrown
        g2D.setPaint(brown);
        g2D.fillRect(0, 0, getWidth(), getHeight());

        // Draw 3 wheels
        g2D.setPaint(Color.WHITE);
        g2D.fillRect(120, 100, 50, 200);
        g2D.fillRect(270, 100, 50, 200);
        g2D.fillRect(420, 100, 50, 200);

        g2D.setPaint(Color.BLACK);
        g2D.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics fm = g2D.getFontMetrics();

// Draw wheel1 centered
        String w1 = String.valueOf(wheel1);
        int w1Width = fm.stringWidth(w1);
        g2D.drawString(w1, 120 + 25 - w1Width / 2, 200);

// Draw wheel2 centered
        String w2 = String.valueOf(wheel2);
        int w2Width = fm.stringWidth(w2);
        g2D.drawString(w2, 270 + 25 - w2Width / 2, 200);

// Draw wheel3 centered
        String w3 = String.valueOf(wheel3);
        int w3Width = fm.stringWidth(w3);
        g2D.drawString(w3, 420 + 25 - w3Width / 2, 200);


        // --- PEG BOARD ---
        int startX = 60;
        int startY = 450;
        int cols = 9;
        int spacing = 60;
        int pegRadius = 20;
        pegPositions.clear();

        for (int i = 0; i < 26; i++) {
            char letter = (char) ('A' + i);
            int row = i / cols;
            int col = i % cols;
            int x = startX + col * spacing;
            int y = startY + row * spacing;

            pegPositions.put(letter, new Point(x, y));

            Color pegColor = pegColors.getOrDefault(letter, Color.GRAY);
            g2D.setColor(pegColor);
            g2D.fillOval(x - pegRadius, y - pegRadius, pegRadius * 2, pegRadius * 2);
            g2D.setColor(Color.BLACK);
            g2D.drawOval(x - pegRadius, y - pegRadius, pegRadius * 2, pegRadius * 2);

            g2D.setFont(new Font("SansSerif", Font.BOLD, 16));
            FontMetrics Fm = g2D.getFontMetrics();
            int textWidth = Fm.stringWidth("" + letter);
            g2D.drawString("" + letter, x - textWidth / 2, y + 5);
        }

        // Draw peg pair lines
        for (Map.Entry<Character, Character> entry : pegPairs.entrySet()) {
            char a = entry.getKey();
            char b = entry.getValue();
            if (a < b && pegPositions.containsKey(a) && pegPositions.containsKey(b)) {
                Point p1 = pegPositions.get(a);
                Point p2 = pegPositions.get(b);
                g2D.setColor(pegColors.get(a));
                g2D.setStroke(new BasicStroke(3));
                g2D.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    private void handlePegClick(char peg) {
        if (firstClicked == null) {
            firstClicked = peg;
        } else if (firstClicked != peg) {
            unpairPeg(firstClicked);
            unpairPeg(peg);

            Color color = colorPool.get(new Random().nextInt(colorPool.size()));
            pegPairs.put(firstClicked, peg);
            pegPairs.put(peg, firstClicked);
            pegColors.put(firstClicked, color);
            pegColors.put(peg, color);

            firstClicked = null;
        } else {
            firstClicked = null;
        }
        repaint();
    }

    private void unpairPeg(char peg) {
        if (pegPairs.containsKey(peg)) {
            char other = pegPairs.get(peg);
            pegPairs.remove(peg);
            pegPairs.remove(other);
            pegColors.remove(peg);
            pegColors.remove(other);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (Map.Entry<Character, Point> entry : pegPositions.entrySet()) {
            char letter = entry.getKey();
            Point p = entry.getValue();
            int dx = e.getX() - p.x;
            int dy = e.getY() - p.y;
            if (dx * dx + dy * dy <= 400) {
                handlePegClick(letter);
                break;
            }
        }
    }

    // Unused MouseEvents
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Manual Wheel + Peg Board");
        ManualWheelAndPegBoard panel = new ManualWheelAndPegBoard(new int[] {1, 1, 1}, frame);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 750);
        frame.setVisible(true);
    }
    /*
    public int getWheel1() {
        return wheel1;
    }
    public int getWheel2() {
        return wheel2;
    }
    public int getWheel3() {
        return wheel3;
    }

     */
}
