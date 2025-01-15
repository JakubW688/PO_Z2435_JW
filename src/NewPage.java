import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import com.myapp.database.DatabaseHelper;
import com.myapp.utils.PriceCalculator;

public class NewPage extends JFrame {
    private static final String FRAME_TITLE = "Kategorie";
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 800;

    public NewPage() {
        setTitle(FRAME_TITLE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BackgroundPanel backgroundPanel = new BackgroundPanel("Images/tło.png");
            backgroundPanel.setLayout(null);

            addPageTitle(backgroundPanel);
            addDateTimeLabel(backgroundPanel);
            addCategoryButtons(backgroundPanel);

            setContentPane(backgroundPanel);
        } catch (IOException e) {
            showErrorAndExit("Błąd wczytywania obrazu tła: " + e.getMessage());
        }
    }

    private void addPageTitle(JPanel panel) {
        JLabel pageTitle = new JLabel(FRAME_TITLE, SwingConstants.CENTER);
        pageTitle.setFont(createFont(24));
        pageTitle.setForeground(Color.DARK_GRAY);
        pageTitle.setBounds(0, 10, FRAME_WIDTH, 30);
        panel.add(pageTitle);
    }

    private void addDateTimeLabel(JPanel panel) {
        JLabel dateTimeLabel = new JLabel();
        dateTimeLabel.setFont(createFont(14));
        dateTimeLabel.setForeground(Color.BLACK);
        dateTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dateTimeLabel.setBounds(FRAME_WIDTH - 200, 10, 180, 30);
        updateDateTime(dateTimeLabel);
        panel.add(dateTimeLabel);
    }

    private void addCategoryButtons(JPanel panel) {
        JButton button1 = createCategoryButton("Konsole", 300, 300, "Images/Konsole.png", "bazy_danych/baza_danych_konsole.txt");
        JButton button2 = createCategoryButton("Gry", 300, 360, "Images/Gry.jpg", "bazy_danych/baza_danych_gry.txt");

        panel.add(button1);
        panel.add(button2);
    }

    private JButton createCategoryButton(String label, int x, int y, String imagePath, String databasePath) {
        JButton button = new JButton(label);
        button.setBounds(x, y, 200, 40);
        button.addActionListener(e -> openNewPage(label, imagePath, databasePath));
        return button;
    }

    private Font createFont(int size) {
        return new Font("Serif", Font.PLAIN, size);
    }

    private void updateDateTime(JLabel label) {
        Timer timer = new Timer();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> label.setText(formatter.format(new Date())));
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

                addMenuToFrame(newFrame);
                addLabelToPanel(pageTitle, backgroundPanel);

                newFrame.setContentPane(backgroundPanel);
                newFrame.setVisible(true);
            } catch (IOException e) {
                showError("Błąd wczytywania obrazu tła: " + e.getMessage());
            }
        });
    }

    private void addMenuToFrame(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opcje");

        JMenuItem addItem = new JMenuItem("Dodaj produkt");
        addItem.addActionListener(e -> addProductAction(frame));
        menu.add(addItem);

        JMenuItem removeItem = new JMenuItem("Usuń produkt");
        removeItem.addActionListener(e -> removeProductAction(frame));
        menu.add(removeItem);

        JMenuItem searchItem = new JMenuItem("Wyszukaj produkt");
        searchItem.addActionListener(e -> searchProductAction(frame));
        menu.add(searchItem);

        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    private void addLabelToPanel(String text, JPanel panel) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(createFont(20));
        label.setBounds(50, 50, 300, 30);
        panel.add(label);
    }

    private void addProductAction(JFrame frame) {
        String productName = JOptionPane.showInputDialog(frame, "Podaj nazwę produktu:");
        String productPrice = JOptionPane.showInputDialog(frame, "Podaj cenę netto:");

        if (isInputValid(productName, productPrice)) {
            try {
                double nettoPrice = Double.parseDouble(productPrice);
                double grossPrice = PriceCalculator.calculateGrossPrice(nettoPrice);
                DatabaseHelper.saveProductWithGrossPrice(productName, nettoPrice, grossPrice);
                showMessage(frame, String.format("Produkt dodany pomyślnie!\nNazwa: %s\nCena netto: %.2f PLN\nCena brutto: %.2f PLN", productName, nettoPrice, grossPrice));
            } catch (NumberFormatException ex) {
                showError("Cena musi być liczbą.");
            }
        } else {
            showError("Nie podano nazwy lub ceny produktu!");
        }
    }

    private void removeProductAction(JFrame frame) {
        String productNameToDelete = JOptionPane.showInputDialog(frame, "Podaj nazwę produktu do usunięcia:");
        if (productNameToDelete == null || productNameToDelete.isBlank()) {
            showError("Nie podano nazwy produktu.");
        } else if (DatabaseHelper.deleteProduct(productNameToDelete)) {
            showMessage(frame, "Produkt usunięty pomyślnie!");
        } else {
            showError("Nie znaleziono produktu o nazwie: " + productNameToDelete);
        }
    }

    private void searchProductAction(JFrame frame) {
        String searchTerm = JOptionPane.showInputDialog(frame, "Podaj frazę do wyszukania:");
        if (searchTerm == null || searchTerm.isBlank()) {
            showError("Nie podano frazy do wyszukania.");
        } else {
            String results = DatabaseHelper.searchProducts(searchTerm);
            showMessage(frame, results.isEmpty() ? "Brak wyników dla: " + searchTerm : results);
        }
    }

    private boolean isInputValid(String name, String price) {
        return name != null && !name.isBlank() && price != null && !price.isBlank();
    }

    private void showMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

    private void showErrorAndExit(String message) {
        showError(message);
        System.exit(1);
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
