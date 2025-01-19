import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class LoginAction implements ActionListener {
    private JTextField login;
    private JTextField haslo;

    public LoginAction(JTextField login, JTextField haslo) {
        this.login = login;
        this.haslo = haslo;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String userValue = login.getText();
        String passValue = haslo.getText();

        if (userValue.equals("admin") && passValue.equals("admin")) {
            NewPage page = new NewPage();
            page.setVisible(true);

        } else {
            System.out.println("Nazwa użytkownika lub hasło jest nieprawidłowe");
            JOptionPane.showMessageDialog(null, "Nazwa użytkownika lub hasło jest nieprawidłowe");
        }
    }
}
