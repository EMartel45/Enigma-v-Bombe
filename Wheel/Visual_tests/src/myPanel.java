import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class myPanel extends JPanel {

    private int wheel1 = 1;
    private int wheel2 = 1;
    private int wheel3 = 1;

    public myPanel() {
        this.setPreferredSize(new Dimension(500, 600));
        this.setLayout(null); // We'll manually place buttons

        // Buttons for Wheel 1
        JButton up1 = new JButton("↑");
        JButton down1 = new JButton("↓");
        up1.setBounds(100, 320, 50, 30);
        down1.setBounds(100, 360, 50, 30);
        this.add(up1);
        this.add(down1);

        // Buttons for Wheel 2
        JButton up2 = new JButton("↑");
        JButton down2 = new JButton("↓");
        up2.setBounds(200, 320, 50, 30);
        down2.setBounds(200, 360, 50, 30);
        this.add(up2);
        this.add(down2);

        // Buttons for Wheel 3
        JButton up3 = new JButton("↑");
        JButton down3 = new JButton("↓");
        up3.setBounds(300, 320, 50, 30);
        down3.setBounds(300, 360, 50, 30);
        this.add(up3);
        this.add(down3);

        // Action listeners
        up1.addActionListener(e -> {
            wheel1 = wheel1 == 26 ? 1 : wheel1 + 1;
            repaint();
        });
        down1.addActionListener(e -> {
            wheel1 = wheel1 == 1 ? 26 : wheel1 - 1;
            repaint();
        });

        up2.addActionListener(e -> {
            wheel2 = wheel2 == 26 ? 1 : wheel2 + 1;
            repaint();
        });
        down2.addActionListener(e -> {
            wheel2 = wheel2 == 1 ? 26 : wheel2 - 1;
            repaint();
        });

        up3.addActionListener(e -> {
            wheel3 = wheel3 == 26 ? 1 : wheel3 + 1;
            repaint();
        });
        down3.addActionListener(e -> {
            wheel3 = wheel3 == 1 ? 26 : wheel3 - 1;
            repaint();
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        // Background
        Color brown = new Color(139, 69, 19); // SaddleBrown
        g2D.setPaint(brown);
        g2D.fillRect(0, 0, 500, 600);

        // Wheels
        g2D.setPaint(Color.white);
        g2D.fillRect(100, 100, 50, 200); // Wheel 1
        g2D.fillRect(200, 100, 50, 200); // Wheel 2
        g2D.fillRect(300, 100, 50, 200); // Wheel 3

        // Numbers
        g2D.setPaint(Color.black);
        g2D.setFont(new Font("Arial", Font.BOLD, 36));
        g2D.drawString(String.valueOf(wheel1), 115, 200);
        g2D.drawString(String.valueOf(wheel2), 215, 200);
        g2D.drawString(String.valueOf(wheel3), 315, 200);
    }

    // Main to test the panel
    public static void main(String[] args) {
        JFrame frame = new JFrame("Wheel Spinner");
        myPanel panel = new myPanel();
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}




//import org.w3c.dom.css.RGBColor;
//
//import javax.swing.JPanel;
//import java.awt.*;
//
//public class myPanel extends JPanel {
//
//    myPanel(){
//        this.setPreferredSize(new Dimension(500, 500));
//    }
//    public void paint(Graphics g){
//
//        Graphics2D g2D = (Graphics2D) g;
//
//
//        Color brown = new Color(139, 69, 19); // SaddleBrown
//        g2D.setPaint(brown);
//        g2D.setStroke(new BasicStroke(10));
//        //g2D.drawLine(0, 0, 500, 500);
//
//        g2D.fillRect(0,0,500,500);
//
//        g2D.setPaint(Color.white);
//        g2D.fillRect(125, 100, 50, 200 );
//
//        g2D.setPaint(Color.white);
//        g2D.fillRect(225, 100, 50, 200 );
//
//        g2D.setPaint(Color.white);
//        g2D.fillRect(325, 100, 50, 200 );
//
//
//    }
//}

