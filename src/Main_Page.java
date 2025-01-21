import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main_Page {
    public static final String FRAME_TITLE = "Projekt";
    public static final String CLOSE_BUTTON_TEXT = "Wyjście";
    public static final String LOGIN_BUTTON_TEXT = "Logowanie";

    public static class BackgroundFrame extends JFrame {
        private BufferedImage backgroundImage;

        public BackgroundFrame() {
            try {
                backgroundImage = ImageIO.read(new File("Images/mainPage.png"));
            } catch (IOException e) {
                System.err.println("Błąd wczytywania obrazu tła: " + e.getMessage());
            }

            setTitle(FRAME_TITLE);
            setSize(800, 800);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            BackgroundPanel backgroundPanel = new BackgroundPanel();
            backgroundPanel.setLayout(null);
            setContentPane(backgroundPanel);

            JPanel controlPanel = createControlPanel();
            controlPanel.setOpaque(false);
            controlPanel.setBounds(0, getHeight() - 100, getWidth(), 100);
            backgroundPanel.add(controlPanel);

            JPanel loginControlPanel = createLoginPanel();
            loginControlPanel.setOpaque(false);
            loginControlPanel.setBounds(0, getHeight() - 450, getWidth(), 300);
            backgroundPanel.add(loginControlPanel);

            setVisible(true);
        }

        private class BackgroundPanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        }
    }

    private static JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JButton closeButton = new JButton(CLOSE_BUTTON_TEXT);
        closeButton.addActionListener(e -> System.exit(0));
        getSerif(closeButton, 15);
        panel.add(closeButton);

        return panel;
    }

    private static void getSerif(JButton closeButton, int size) {
        closeButton.setFont(new Font("Serif", Font.PLAIN, size));
    }

    private static JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JButton loginButton = new JButton(LOGIN_BUTTON_TEXT);
        loginButton.addActionListener(e -> new CreateLoginForm().setVisible(true));
        getSerif(loginButton, 20);
        panel.add(loginButton);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BackgroundFrame::new);
    }
}
