import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NewPage extends JFrame {
    public static final String FRAME_TITLE_2 = "";

    public NewPage() {
        setTitle(FRAME_TITLE_2);
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BackgroundPanel backgroundPanel = new BackgroundPanel("Images/tło.png");
            backgroundPanel.setLayout(null);

            JLabel pageTitle = new JLabel(FRAME_TITLE_2, SwingConstants.CENTER);
            pageTitle.setFont(new Font("Serif", Font.PLAIN, 24));
            pageTitle.setForeground(Color.WHITE);
            pageTitle.setBounds(0, 10, 800, 30);
            backgroundPanel.add(pageTitle);

            JLabel categoryLabel = new JLabel("Kategorie", SwingConstants.CENTER);
            categoryLabel.setFont(new Font("Serif", Font.PLAIN, 18));
            categoryLabel.setForeground(Color.BLACK);
            categoryLabel.setBounds(0, 50, 800, 30);
            backgroundPanel.add(categoryLabel);

            JButton button1 = new JButton("Konsole");
            button1.setBounds(300, 300, 200, 40);
            button1.addActionListener(e -> openNewPage("Konsole"));
            backgroundPanel.add(button1);

            JButton button2 = new JButton("Gry");
            button2.setBounds(300, 360, 200, 40);
            button2.addActionListener(e -> openNewPage("Gry"));
            backgroundPanel.add(button2);

            setContentPane(backgroundPanel);
        } catch (IOException e) {
            System.err.println("Błąd wczytywania obrazu tła: " + e.getMessage());
            System.exit(1);
        }
    }

    private void openNewPage(String pageTitle) {
        SwingUtilities.invokeLater(() -> {
            JFrame newFrame = new JFrame(pageTitle);
            newFrame.setSize(400, 400);
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newFrame.setLocationRelativeTo(this);
            JLabel label = new JLabel(pageTitle, SwingConstants.CENTER);
            label.setFont(new Font("Serif", Font.PLAIN, 20));
            newFrame.add(label);
            newFrame.setVisible(true);
        });
    }

    private static class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundPanel(String imagePath) throws IOException {
            backgroundImage = ImageIO.read(new File(imagePath));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NewPage frame = new NewPage();
            frame.setVisible(true);
        });
    }
}
