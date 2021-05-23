package FrontEnd;

import Entity.GradeCustomObject;
import Entity.Group;
import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainPage {
    private JPanel mainPagePanel;
    private JPanel configPanel;
    private JComboBox groupComboBox;
    private JScrollPane contentPanel;
    private JLabel groupLabel;
    private JTable gradeTable;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Gson gson;
    private Object[][] tableData;
    private String[] tableHeader;

    public MainPage() {
        groupComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String group = (String) groupComboBox.getSelectedItem();

                String commandAndJson = "group-student get all {\"name\":\"" + group + "\"}";
                out.println(commandAndJson);
                String response = "";
                try {
                    response = in.readLine();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                response = response.substring(response.indexOf("["));
                String[] students = gson.fromJson(response, String[].class);
                System.out.print("Students from " + group + ": ");
                for (String student : students)
                    System.out.print(student + " ");
                System.out.println();

                commandAndJson = "group-task get all {\"name\":\"" + group + "\"}";
                out.println(commandAndJson);
                try {
                    response = in.readLine();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                response = response.substring(response.indexOf("["));
                String[] tasks = gson.fromJson(response, String[].class);
                System.out.print("Tasks for " + group + ": ");
                for (String task : tasks)
                    System.out.print(task + " ");
                System.out.println();

                commandAndJson = "grade get all {\"name\":\"" + group + "\"}";
                out.println(commandAndJson);
                try {
                    response = in.readLine();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                response = response.substring(response.indexOf("["));
                GradeCustomObject[] gradeList = gson.fromJson(response, GradeCustomObject[].class);


                String[] tableHeader = new String[tasks.length + 1];
                tableHeader[0] = "Students";
                System.arraycopy(tasks, 0, tableHeader, 1, tasks.length);

                Object[][] tableData = new Object[students.length][tableHeader.length];

                for (int i = 0; i < students.length; i++)
                    tableData[i][0] = students[i];

                for (int j = 0; j < tasks.length; j++) {
                    for (int i = 0; i < gradeList[j].getGrade().size(); i++)
                        tableData[i][j+1] = gradeList[j].getGrade().get(i);
                }

                for (int i = 0; i < students.length; i++) {
                    for (int j = 0; j < tableHeader.length; j++)
                        System.out.print(tableData[i][j] + " ");
                    System.out.println();
                }
                createTableData(tableHeader,tableData);

            }
        });
    }

    private void createTableData(String[] tableHeader, Object[][] tableData)
    {
        TableModel tableModel = new TableModel() {
            @Override
            public int getRowCount() {
                return tableData.length;
            }

            @Override
            public int getColumnCount() {
                return tableHeader.length;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return tableHeader[columnIndex];
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return getValueAt(0,columnIndex).getClass();
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return tableData[rowIndex][columnIndex];
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

            }

            @Override
            public void addTableModelListener(TableModelListener l) {

            }

            @Override
            public void removeTableModelListener(TableModelListener l) {

            }
        };
        this.gradeTable.setModel(tableModel);
    }

    private void initFrame(JFrame frame, MainPage MainPage) {
        frame.setContentPane(MainPage.mainPagePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String response = null;
        String request = "group get all";
        out.println(request);
        try {
            response = in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        response = response.substring(response.indexOf("["));
        System.out.println(response);


        Group[] groups = gson.fromJson(response, Group[].class);
        String[] groupNames = new String[groups.length];
        for (int i = 0; i < groups.length; i++)
            groupNames[i] = groups[i].getName();

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(groupNames);
        this.groupComboBox.setModel(model);
        frame.pack();
    }

    private void setFrameLocation(JFrame frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setVisible(true);
    }

    public void showMainPage(Socket socket) {

        this.socket = socket;
        try {
            out = new PrintWriter(this.socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (
                UnknownHostException e) {
            System.err.println("No server listening... " + e);
        } catch (
                SocketException e) {
            System.err.println("Socket exception. Probably timeout LOL");
        } catch (
                IOException e) {
            System.err.println("IO Exception...");
        }

        gson = new Gson();

        JFrame mainPageFrame = new JFrame("Main Page");
        this.initFrame(mainPageFrame, this);
        this.setFrameLocation(mainPageFrame);
    }

    public static void main(String[] args) {

    }


}
