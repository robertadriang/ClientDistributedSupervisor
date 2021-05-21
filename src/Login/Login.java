package Login;

import javax.swing.*;
import java.awt.*;

public class Login {
    private  JButton loginButton;
    private  JButton registerButton;
    private  JPanel loginPanel;
    private  JPanel titlePanel;
    private  JPanel buttonPanel;
    private  JPanel loginFormPanel;
    private  JTextField userTextField;
    private  JComboBox userTypeComboBox;
    private  JLabel userLabel;
    private  JLabel userTypeLabel;

    private void initFrame(JFrame frame)
    {
        frame.setContentPane(new Login().loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    private void setFrameLocation(JFrame frame)
    {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame loginFrame = new JFrame("Login");
        Login login = new Login();
        login.initFrame(loginFrame);
        login.setFrameLocation(loginFrame);
    }
}
