package FrontEnd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Login {
    private static final int PORT = 8101;
    private static final String serverAddress = "127.0.0.1";
    private Socket socket;
    private JFrame loginFrame;
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

        this.loginFrame = new JFrame("FrontEnd");

        PrintWriter out = null;
        BufferedReader in = null;

        try {
            this.socket = new Socket(serverAddress, PORT);
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
                    System.out.println(response);
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
                    System.out.println(response);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                if(response.contains("logged in!"))
                    login();
                else
                    JOptionPane.showMessageDialog(null,response);
            }
        });

        userTextField.addKeyListener(new KeyAdapter() {         //Added login on ENTER key
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_ENTER) {
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
                        System.out.println(response);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    if(response.contains("logged in!"))
                        login();
                    else
                        JOptionPane.showMessageDialog(null,response);
                }
            }
        });
    }

    private void login()
    {
        this.loginFrame.setVisible(false);
        MainPage mainPage = new MainPage();
        mainPage.showMainPage(this.socket);

    }

    private void initFrame(Login login)
    {
        this.loginFrame.setContentPane(login.loginPanel);
        this.loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.loginFrame.pack();
    }

    private void setFrameLocation()
    {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.loginFrame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.loginFrame.getHeight()) / 2);
        this.loginFrame.setLocation(x, y);
        this.loginFrame.setVisible(true);
    }

    public static void main(String[] args) {

        Login login = new Login();
        login.initFrame(login);
        login.setFrameLocation();
    }
}
