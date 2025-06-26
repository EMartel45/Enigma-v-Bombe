import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class ManualWheelAndPegBoard extends JPanel implements MouseListener {
    private final Map<Character, Point> pegPositions = new HashMap<>();
    private final Map<Character, Character> pegPairs = new HashMap<>();
    private final Map<Character, Color> pegColors = new HashMap<>();
    private final List<Color> colorPool = new ArrayList<>();
    private Character firstClicked = null;

    private int wheel1 = 1;
    private int wheel2 = 1;
    private int wheel3 = 1;

    public ManualWheelAndPegBoard() {
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
        });
        down1.addActionListener(e -> {
            wheel1 = wheel1 == 1 ? 26 : wheel1 - 1;
            repaint();
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
        });
        down2.addActionListener(e -> {
            wheel2 = wheel2 == 1 ? 26 : wheel2 - 1;
            repaint();
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
        });
        down3.addActionListener(e -> {
            wheel3 = wheel3 == 1 ? 26 : wheel3 - 1;
            repaint();
        });
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
        g2D.drawString(String.valueOf(wheel1), 135, 200);
        g2D.drawString(String.valueOf(wheel2), 285, 200);
        g2D.drawString(String.valueOf(wheel3), 435, 200);

        // --- PEG BOARD ---
        int startX = 60;
        int startY = 450;
        int cols = 13;
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
            FontMetrics fm = g2D.getFontMetrics();
            int textWidth = fm.stringWidth("" + letter);
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
        ManualWheelAndPegBoard panel = new ManualWheelAndPegBoard();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 750);
        frame.setVisible(true);
    }
}
