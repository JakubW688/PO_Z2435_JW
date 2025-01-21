import javax.swing.*;
import java.awt.*;

public class CreateLoginForm extends JFrame {
    private JButton loginButton;
    private JPanel panel;
    private JLabel userLabel, passLabel;
    private JTextField loginField;
    private JPasswordField passwordField;

    public CreateLoginForm() {

        userLabel = new JLabel("Nazwa użytkownika:");
        loginField = new JTextField(15);

        passLabel = new JLabel("Hasło:");
        passwordField = new JPasswordField(15);

        loginButton = new JButton("Zaloguj");
        loginButton.addActionListener(new LoginAction(loginField, passwordField));

        panel = new JPanel(new GridLayout(3, 2));
        panel.add(userLabel);
        panel.add(loginField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        add(panel, BorderLayout.CENTER);

        setTitle("Logowanie");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}

