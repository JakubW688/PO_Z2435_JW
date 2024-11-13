import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NewPage extends JFrame {
    public static final String FRAME_TITLE_2 = "Magazyn";
    public static final String RETURN_BUTTON = "Wróć";


    class Background_Frame extends JFrame {
        private BufferedImage backgroundImage2;

        public BACKGROUND_FRAME2() {
            try {
                backgroundImage2 = ImageIO.read(new File("Images/tło.png"));
            } catch (IOException e) {
                System.err.println("Błąd wczytywania obrazu tła: " + e.getMessage());
                System.exit(1);
            }
            setTitle(FRAME_TITLE_2);
            setSize(800, 800);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            BackgroundPanel backgroundPanel = new BackgroundPanel();
            backgroundPanel.setLayout(new BorderLayout());
            backgroundPanel.setLayout(null);
            setContentPane(backgroundPanel);


        }

        private class BackgroundPanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage2 != null) {
                    g.drawImage(backgroundImage2, 0, 0, getWidth(), getHeight(), this);
                }
            }
        }
    }
}
