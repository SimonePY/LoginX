import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Arrays;

public class LoginApp extends JDialog {
    private User user;
    private JTextField emailText;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JPanel loginForm;

    public LoginApp(JFrame parent) {
        super(parent);
        setTitle("LoginX");
        setContentPane(loginForm);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        loginButton.addActionListener(e -> {
            String email = emailText.getText().trim();
            char[] password = passwordField.getPassword();
            user = getAuthenticatedUser(email, password);
            if (user != null) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid email or password.",
                        "Authentication Failed.",
                        JOptionPane.ERROR_MESSAGE);
            }
            Arrays.fill(password, ' ');
        });
        cancelButton.addActionListener(e -> dispose());
        setVisible(true);
    }

    public static void main(String[] args) {
        LoginApp loginApp = new LoginApp(null);
        User user = loginApp.user;
        if (user != null) {
            System.out.println("Successful Authentication for: " + user.name);
            System.out.println("Email: " + user.email);
            System.out.println("Phone: " + user.phone);
            System.out.println("Address: " + user.address);
        } else {
            System.out.println("Authentication Failed.");
        }
    }

    private User getAuthenticatedUser(String email, char[] password) {
        User user = null;
        final String dbUrl = "";
        final String username = "";
        final String passwordString = "";
        try (Connection conn = DriverManager.getConnection(dbUrl, username, passwordString);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?")) {
            stmt.setString(1, email);
            stmt.setString(2, new String(password));
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    private static class User {
        public String name;
        public String email;
        public String phone;
        public String address;
        public String password;
    }
}
