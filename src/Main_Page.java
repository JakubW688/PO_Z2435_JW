import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import com.myapp.database.DatabaseHelper;

public class Main_Page {
    public static final String FRAME_TITLE = "Projekt";
    public static final String CLOSE_BUTTON_TEXT = "Wyjście";
    public static final String LOGIN_BUTTON_TEXT = "Logowanie";
    public static final String VIEW_PRODUCTS_BUTTON_TEXT = "Magazyn"; 

    class CreateLoginForm extends JFrame implements ActionListener{
        JButton b1;
        JPanel panel;
        JLabel userLabel, passLabel;
        JTextField login, haslo;
        public CreateLoginForm(){
            userLabel = new JLabel();
            userLabel.setText("Nazwa użytkownika");
            login = new JTextField(15);
            passLabel = new JLabel();
            passLabel.setText("Hasło");
            haslo = new JTextField(15);
            b1 = new JButton("Zaloguj");
            b1.addActionListener(new LoginAction(login, haslo));
            panel = new JPanel(new GridLayout(3,1));
            panel.add(userLabel);
            panel.add(login);
            panel.add(passLabel);
            panel.add(haslo);
            panel.add(b1);
            add(panel, BorderLayout.CENTER);
            setTitle("Logowanie");
            setSize(300,150);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    public static class BackgroundFrame extends JFrame {
        private BufferedImage backgroundImage;

        public BackgroundFrame() {
            try {
                backgroundImage = ImageIO.read(new File("Images/mainPage.png"));
            } catch (IOException e) {
                System.err.println("Błąd wczytywania obrazu tła: " + e.getMessage());
                System.exit(1);
            }

            setTitle(FRAME_TITLE);
            setSize(800, 800);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            BackgroundPanel backgroundPanel = new BackgroundPanel();
            backgroundPanel.setLayout(new BorderLayout());
            backgroundPanel.setLayout(null);
            setContentPane(backgroundPanel);

            JPanel controlPanel = createControlPanel();
            controlPanel.setOpaque(false);
            controlPanel.setBounds(1,getHeight()-100, getWidth(),100);
            backgroundPanel.add(controlPanel);
            setVisible(true);

            JPanel loginControlPanel = createLoginPanel();
            loginControlPanel.setOpaque(false);
            loginControlPanel.setBounds(1,getHeight()-450, getWidth(),300);
            backgroundPanel.add(loginControlPanel);
            setVisible(true);


            DatabaseHelper.initializeDatabase();
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
        closeButton.setFont(new Font("Serif", Font.PLAIN,15));
        panel.add(closeButton);

        JButton viewProductsButton = new JButton(VIEW_PRODUCTS_BUTTON_TEXT);
        viewProductsButton.addActionListener(e -> {
            String allProducts = DatabaseHelper.getAllProducts();
            JOptionPane.showMessageDialog(null, allProducts.isEmpty() ? "Brak produktów w bazie danych" : allProducts);
        });
        panel.add(viewProductsButton);
        return panel;
    }

    private static JPanel createLoginPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JButton loginButton = new JButton(LOGIN_BUTTON_TEXT);
        loginButton.addActionListener( e -> new Main_Page().new CreateLoginForm().setVisible(true));
        loginButton.setFont(new Font("Serif", Font.PLAIN,20));
        panel.add(loginButton);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BackgroundFrame::new);
    }
}
