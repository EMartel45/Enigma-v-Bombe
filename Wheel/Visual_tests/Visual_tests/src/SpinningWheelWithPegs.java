import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class SpinningWheelWithPegs extends JPanel implements ActionListener, KeyListener, MouseListener {
    private int faceCount = 8;
    private double angle = 0;
    private final Timer timer;

    private final Map<Character, Point> pegPositions = new HashMap<>();
    private final Map<Character, Character> pegPairs = new HashMap<>();
    private final Map<Character, Color> pegColors = new HashMap<>();
    private final List<Color> colorPool = new ArrayList<>();
    private Character firstClicked = null;

    public SpinningWheelWithPegs() {
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        timer = new Timer(30, this);
        timer.start();
        initColors();
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
        int width = getWidth();
        int height = getHeight();

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw spinning wheel
        int radius = Math.min(width, height) / 4;
        int centerX = width / 2;
        int centerY = height / 3;

        g2D.setColor(Color.LIGHT_GRAY);
        g2D.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        g2D.setColor(Color.DARK_GRAY);
        g2D.fillOval(centerX - 10, centerY - 10, 20, 20);

        for (int i = 0; i < faceCount; i++) {
            double theta = angle + (2 * Math.PI / faceCount) * i;
            int x = centerX + (int) (Math.cos(theta) * radius * 0.75);
            int y = centerY + (int) (Math.sin(theta) * radius * 0.75);

            g2D.setColor(Color.ORANGE);
            g2D.fillOval(x - 20, y - 20, 40, 40);
            g2D.setColor(Color.BLACK);
            g2D.drawOval(x - 20, y - 20, 40, 40);

            String label = String.valueOf((i % 26) + 1);
            g2D.setFont(new Font("SansSerif", Font.BOLD, 14));
            FontMetrics fm = g2D.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            int textHeight = fm.getAscent();
            g2D.drawString(label, x - textWidth / 2, y + textHeight / 4);
        }

        // Draw pegboard
        int startX = 50;
        int startY = height - 200;
        int cols = 13;
        int spacing = 40;
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
            g2D.fillOval(x - 12, y - 12, 24, 24);
            g2D.setColor(Color.BLACK);
            g2D.drawOval(x - 12, y - 12, 24, 24);

            g2D.setFont(new Font("SansSerif", Font.BOLD, 14));
            FontMetrics fm = g2D.getFontMetrics();
            int textWidth = fm.stringWidth("" + letter);
            g2D.drawString("" + letter, x - textWidth / 2, y + 5);
        }

        // Draw lines for paired pegs
        for (Map.Entry<Character, Character> entry : pegPairs.entrySet()) {
            char a = entry.getKey();
            char b = entry.getValue();
            if (a < b && pegPositions.containsKey(a) && pegPositions.containsKey(b)) {
                Point p1 = pegPositions.get(a);
                Point p2 = pegPositions.get(b);
                g2D.setColor(pegColors.get(a));
                g2D.setStroke(new BasicStroke(2));
                g2D.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        angle += 0.02;
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP && faceCount < 26) {
            faceCount++;
        } else if (code == KeyEvent.VK_DOWN && faceCount > 1) {
            faceCount--;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (Map.Entry<Character, Point> entry : pegPositions.entrySet()) {
            char letter = entry.getKey();
            Point p = entry.getValue();
            int dx = e.getX() - p.x;
            int dy = e.getY() - p.y;
            if (dx * dx + dy * dy <= 12 * 12) { // Clicked inside the circle
                handlePegClick(letter);
                break;
            }
        }
    }

    private void handlePegClick(char peg) {
        if (firstClicked == null) {
            firstClicked = peg;
        } else if (firstClicked != peg) {
            // Remove previous connections to avoid duplicates
            unpairPeg(firstClicked);
            unpairPeg(peg);

            // Assign a random color from pool
            Color color = colorPool.get(new Random().nextInt(colorPool.size()));
            pegPairs.put(firstClicked, peg);
            pegPairs.put(peg, firstClicked);
            pegColors.put(firstClicked, color);
            pegColors.put(peg, color);
            firstClicked = null;
        } else {
            firstClicked = null; // Same peg clicked twice
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

    // Unused interface methods
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Spinning Wheel + Pegboard");
        SpinningWheelWithPegs panel = new SpinningWheelWithPegs();
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
