import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class NewPage extends JFrame {
    public static final String FRAME_TITLE_2 = "Kategorie";

    public NewPage() {
        setTitle(FRAME_TITLE_2);
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BackgroundPanel backgroundPanel = new BackgroundPanel("Images/tło.png");
            backgroundPanel.setLayout(null);

            JLabel pageTitle = new JLabel(FRAME_TITLE_2, SwingConstants.CENTER);
            pageTitle.setFont(new Font("Serif", Font.PLAIN, 24));
            pageTitle.setForeground(Color.DARK_GRAY);
            pageTitle.setBounds(0, 10, 800, 30);
            backgroundPanel.add(pageTitle);

            JLabel categoryLabel = new JLabel(" ", SwingConstants.CENTER);
            categoryLabel.setFont(new Font("Serif", Font.PLAIN, 18));
            categoryLabel.setForeground(Color.BLACK);
            categoryLabel.setBounds(0, 50, 800, 30);
            backgroundPanel.add(categoryLabel);

            JButton button1 = new JButton("Konsole");
            button1.setBounds(300, 300, 200, 40);
            button1.addActionListener(e -> openNewPage("", "Images/Konsole.png", "bazy_danych/baza_danych_konsole.txt"));
            backgroundPanel.add(button1);

            JButton button2 = new JButton("Gry");
            button2.setBounds(300, 360, 200, 40);
            button2.addActionListener(e -> openNewPage("", "Images/Gry.jpg", "bazy_danych/baza_danych_gry.txt"));
            backgroundPanel.add(button2);

            setContentPane(backgroundPanel);
        } catch (IOException e) {
            System.err.println("Błąd wczytywania obrazu tła: " + e.getMessage());
            System.exit(1);
        }
    }

    private void openNewPage(String pageTitle, String backgroundImagePath, String databaseFilePath) {
        SwingUtilities.invokeLater(() -> {
            JFrame newFrame = new JFrame(pageTitle);
            newFrame.setSize(400, 400);
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newFrame.setLocationRelativeTo(this);

            try {
                BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImagePath);
                backgroundPanel.setLayout(null);

                JMenuBar menuBar = new JMenuBar();
                JMenu menu = new JMenu("Opcje");

                JMenuItem addItem = new JMenuItem("Dodaj produkt");
                addItem.addActionListener(e -> {
                    String productName = JOptionPane.showInputDialog(newFrame, "Podaj nazwę produktu:");
                    String productPrice = JOptionPane.showInputDialog(newFrame, "Podaj cenę");

                    if (productName != null && productPrice != null && !productName.isBlank() && !productPrice.isBlank()) {
                        String entry = productName + "  ;  " + productPrice;
                        saveToFile(entry, databaseFilePath);
                        JOptionPane.showMessageDialog(newFrame, "Produkt dodany pomyślnie!");
                    } else {
                        JOptionPane.showMessageDialog(newFrame, "Nie podano nazwy lub ceny produktu!", "Błąd", JOptionPane.WARNING_MESSAGE);
                    }
                });
                menu.add(addItem);

                JMenuItem searchItem = new JMenuItem("Wyszukaj produkt");
                searchItem.addActionListener(e -> JOptionPane.showMessageDialog(newFrame, "Wyszukaj produkt: funkcja w przygotowaniu."));
                menu.add(searchItem);

                JMenuItem refreshItem = new JMenuItem("Odśwież");
                refreshItem.addActionListener(e -> JOptionPane.showMessageDialog(newFrame, "Odśwież: funkcja w przygotowaniu."));
                menu.add(refreshItem);

                menuBar.add(menu);
                newFrame.setJMenuBar(menuBar);

                JLabel label = new JLabel(pageTitle, SwingConstants.CENTER);
                label.setFont(new Font("Serif", Font.PLAIN, 20));
                label.setBounds(50, 50, 300, 30);
                backgroundPanel.add(label);

                newFrame.setContentPane(backgroundPanel);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd wczytywania obrazu tła: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }

            newFrame.setVisible(true);
        });
    }

    private void saveToFile(String data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas zapisu do pliku: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
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
