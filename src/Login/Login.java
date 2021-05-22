package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Login {
    private static final int PORT = 8101;
    public static final String serverAddress = "127.0.0.1";
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

    public Login() {

        PrintWriter out = null;
        BufferedReader in = null;

        try {
            Socket socket = new Socket(serverAddress, PORT);
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        } catch (UnknownHostException e) {
            System.err.println("No server listening... " + e);
        } catch (SocketException e) {
            System.err.println("Socket exception. Probably timeout LOL");
        } catch (IOException e){
            System.err.println("IO Exception...");
        }

        PrintWriter finalOut = out;
        BufferedReader finalIn = in;
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userTextField.getText();
                String userType = (String)userTypeComboBox.getSelectedItem();
                String commandAndJSON = "";
                if(userType.equals("Professor"))
                    commandAndJSON  = "register1 {\"username\":\"" + username + "\"}";
                else if(userType.equals("Student"))
                    commandAndJSON  = "register2 {\"username\":\"" + username + "\"}";
                finalOut.println(commandAndJSON);

                String response = null;
                try {
                    response = finalIn.readLine();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                JOptionPane.showMessageDialog(null,response);
            }
        });


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userTextField.getText();
                String userType = (String)userTypeComboBox.getSelectedItem();
                String commandAndJSON = "";
                if(userType.equals("Professor"))
                    commandAndJSON  = "login1 {\"username\":\"" + username + "\"}";
                else if(userType.equals("Student"))
                    commandAndJSON  = "login2 {\"username\":\"" + username + "\"}";
                finalOut.println(commandAndJSON);

                String response = null;
                try {
                    response = finalIn.readLine();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                JOptionPane.showMessageDialog(null,response);
            }
        });
    }

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
