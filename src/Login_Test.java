import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoginAction implements ActionListener {
    private JTextField textField1;
    private JTextField textField2;

    public LoginAction(JTextField textField1, JTextField textField2) {
        this.textField1 = textField1;
        this.textField2 = textField2;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String userValue = textField1.getText();
        String passValue = textField2.getText();

        if (userValue.equals("admin") && passValue.equals("admin")) {
            NewPage page = new NewPage();
            page.setVisible(true);

        } else {
            System.out.println("Nazwa użytkownika lub hasło jest nieprawidłowe");
            JOptionPane.showMessageDialog(null, "Nazwa użytkownika lub hasło jest nieprawidłowe");
        }
    }
}
