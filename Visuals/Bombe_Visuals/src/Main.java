import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Enigma Decoder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel promptLabel = new JLabel("Enter the Enigma Code:");
        promptLabel.setHorizontalAlignment(JLabel.CENTER);

        JTextField codeField = new JTextField();

        JButton decodeButton = new JButton("Decode");

        panel.add(promptLabel);
        panel.add(codeField);
        panel.add(decodeButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);

        decodeButton.addActionListener(e -> {
            String inputCode = codeField.getText().trim();
            if (inputCode.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a code.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Loading screen with spinning wheels
            JDialog loadingDialog = new JDialog(frame, "Decoding...", true);
            loadingDialog.setSize(300, 150);
            loadingDialog.setLayout(new BorderLayout());

            JLabel loadingLabel = new JLabel("Decoding Bombe Machine...");
            loadingLabel.setHorizontalAlignment(JLabel.CENTER);

            JPanel wheelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JLabel[] wheels = new JLabel[5]; // 5 spinning wheels
            for (int i = 0; i < wheels.length; i++) {
                wheels[i] = new JLabel("[ A ]");
                wheels[i].setFont(new Font("Monospaced", Font.BOLD, 24));
                wheelPanel.add(wheels[i]);
            }

            loadingDialog.add(loadingLabel, BorderLayout.NORTH);
            loadingDialog.add(wheelPanel, BorderLayout.CENTER);
            loadingDialog.setLocationRelativeTo(frame);

            // Thread for animation and decoding simulation
            new Thread(() -> {
                Random rand = new Random();
                boolean running = true;

                // Start animation
                Timer animationTimer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        for (JLabel wheel : wheels) {
                            char randomChar = (char) ('A' + rand.nextInt(26));
                            wheel.setText("[ " + randomChar + " ]");
                        }
                    }
                });

                SwingUtilities.invokeLater(() -> {
                    animationTimer.start();
                    loadingDialog.setVisible(true);
                });

                try {
                    Thread.sleep(3000); // simulate decoding duration
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                animationTimer.stop(); // stop the wheel animation

                // Close the dialog and show the result
                SwingUtilities.invokeLater(() -> {
                    loadingDialog.dispose();
                    JOptionPane.showMessageDialog(frame, "Decoded Message:\n" + inputCode,
                            "Result", JOptionPane.INFORMATION_MESSAGE);
                });
            }).start();
        });
    }
}
