import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class PegBoard extends JPanel implements MouseListener {
    private final Map<Character, Point> pegPositions = new HashMap<>();
    private final Map<Character, Character> pegPairs = new HashMap<>();
    private final Map<Character, Color> pegColors = new HashMap<>();
    private final List<Color> colorPool = new ArrayList<>();
    private Character firstClicked = null;

    public PegBoard() {
        setFocusable(true);
        addMouseListener(this);
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
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set peg layout
        int startX = 60;
        int startY = 100;
        int cols = 13;
        int spacing = 60;
        int pegRadius = 20;
        pegPositions.clear();

        // Draw pegs A–Z
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

        // Draw connection lines between pairs
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
            if (dx * dx + dy * dy <= 20 * 20) {
                handlePegClick(letter);
                break;
            }
        }
    }

    // Unused mouse events
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Peg Board (A–Z Matching)");
        PegBoard panel = new PegBoard();
        frame.add(panel);
        frame.setSize(1000, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
