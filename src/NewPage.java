import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NewPage extends JFrame {
    public static final String FRAME_TITLE_2 = "Magazyn";

    public NewPage() {
        setTitle(FRAME_TITLE_2);
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BackgroundPanel backgroundPanel = new BackgroundPanel("Images/tło.png");
            backgroundPanel.setLayout(null);
            setContentPane(backgroundPanel);
        } catch (IOException e) {
            System.err.println("Błąd wczytywania obrazu tła: " + e.getMessage());
            System.exit(1);
        }
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

