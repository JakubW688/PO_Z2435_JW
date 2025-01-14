import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.text.SimpleDateFormat;
import com.myapp.database.DatabaseHelper;
import com.myapp.utils.PriceCalculator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;





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
            pageTitle.setFont(getFont(24));
            pageTitle.setForeground(Color.DARK_GRAY);
            pageTitle.setBounds(0, 10, 800, 30);
            backgroundPanel.add(pageTitle);

            JLabel dateTimeLabel = new JLabel(); // Label for date and time
            dateTimeLabel.setFont(getFont(14));
            dateTimeLabel.setForeground(Color.BLACK);
            dateTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            dateTimeLabel.setBounds(600, 10, 180, 30);
            updateDateTime(dateTimeLabel);
            backgroundPanel.add(dateTimeLabel);

            JLabel categoryLabel = new JLabel("", SwingConstants.CENTER);
            categoryLabel.setFont(getFont(18));
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

    private static Font getFont(int size) {
        return new Font("Serif", Font.PLAIN, size);
    }
    private void updateDateTime(JLabel label) {
        Timer timer = new Timer();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    label.setText(formatter.format(new Date()));
                });
            }
        }, 0, 1000);
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
                    String productPrice = JOptionPane.showInputDialog(newFrame, "Podaj cenę netto:");

                    if (productName != null && productPrice != null && !productName.isBlank() && !productPrice.isBlank()) {
                        try {
                            double nettoPrice = Double.parseDouble(productPrice);
                            double grossPrice = PriceCalculator.calculateGrossPrice(nettoPrice);
                            DatabaseHelper.saveProductWithGrossPrice(productName, nettoPrice, grossPrice);
                            JOptionPane.showMessageDialog(newFrame, String.format("Produkt dodany pomyślnie!\nNazwa: %s\nCena netto: %.2f PLN\nCena brutto: %.2f PLN", productName, nettoPrice, grossPrice));
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(newFrame, "Cena musi być liczbą.", "Błąd", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(newFrame, "Nie podano nazwy lub ceny produktu!", "Błąd", JOptionPane.WARNING_MESSAGE);
                    }
                });


                menu.add(addItem);

                JMenuItem removeItem = new JMenuItem("Usuń produkt");
                removeItem.addActionListener(e -> {
                    String productNameToDelete = JOptionPane.showInputDialog(newFrame, "Podaj nazwę produktu do usunięcia:");
                    if (productNameToDelete == null || productNameToDelete.isBlank()) {
                        JOptionPane.showMessageDialog(newFrame, "Nie podano nazwy produktu.", "Błąd", JOptionPane.WARNING_MESSAGE);
                    } else {
                        boolean isDeleted = DatabaseHelper.deleteProduct(productNameToDelete);
                        if (isDeleted) {
                            JOptionPane.showMessageDialog(newFrame, "Produkt usunięty pomyślnie!");
                        } else {
                            JOptionPane.showMessageDialog(newFrame, "Nie znaleziono produktu o nazwie: " + productNameToDelete, "Błąd", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
                menu.add(removeItem);

                JMenuItem searchItem = new JMenuItem("Wyszukaj produkt");
                searchItem.addActionListener(e -> {
                    String searchTerm = JOptionPane.showInputDialog(newFrame, "Podaj frazę do wyszukania:");
                    if (searchTerm == null || searchTerm.isBlank()) {
                        JOptionPane.showMessageDialog(newFrame, "Nie podano frazy do wyszukania.", "Błąd", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String results = DatabaseHelper.searchProducts(searchTerm);
                        JOptionPane.showMessageDialog(newFrame, results.isEmpty() ? "Brak wyników dla: " + searchTerm : results);
                    }
                });
                menu.add(searchItem);
                menuBar.add(menu);
                newFrame.setJMenuBar(menuBar);

                JLabel label = new JLabel(pageTitle, SwingConstants.CENTER);
                label.setFont(getFont(20));
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
        DatabaseHelper.initializeDatabase();
        SwingUtilities.invokeLater(() -> {
            NewPage frame = new NewPage();
            frame.setVisible(true);
        });
    }
}
