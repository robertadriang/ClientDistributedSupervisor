package FrontEnd;

import Entity.GradeCustomObject;
import Entity.Group;
import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
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
    private JScrollPane scrollPane;
    private JLabel groupLabel;
    private JTable gradeTable;
    private JButton addStudentButton;
    private JLabel tableTitleLabel;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Gson gson;


    public MainPage() {
        groupComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String group = (String) groupComboBox.getSelectedItem();
                createTableData(group);
            }
        });

        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newStudentName = (String) JOptionPane.showInputDialog(
                        null,
                        "Student name: ",
                        "Add Student",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        ""
                );
                if(!newStudentName.isEmpty())
                    addNewStudent(newStudentName);
            }
        });
    }

    private void addNewStudent(String newStudentName) {
        String commandAndJSON = "register2 " + "{\"username\":\"" + newStudentName + "\"}";
        out.println(commandAndJSON);
        String responseCreateStudent = "";
        try {
            responseCreateStudent = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String group = (String) groupComboBox.getSelectedItem();
        String responseGroupStudent = "";
        commandAndJSON = "group-student add ";
        commandAndJSON += "{\"groupname\":\"" + group + "\",\"student\":\"" + newStudentName + "\"}";
        out.println(commandAndJSON);
        try {
            responseGroupStudent = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (responseGroupStudent.contains("group-student created!"))
            createTableData(group);
        else
            JOptionPane.showMessageDialog(null, "Student \"" + newStudentName + "\" already assigned to group \"" + group + "\"!");

    }

    private void createTableData(String group) {
        String commandAndJson = "group-student get all {\"name\":\"" + group + "\"}";
        this.tableTitleLabel.setText("Grade Table - " + group);
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

        String[] tableHeader = new String[tasks.length + 1];
        tableHeader[0] = "Students \\ Tasks";
        for (int i = 0; i < tasks.length; i++)
            tableHeader[i + 1] = tasks[i];

        Object[][] tableData = new Object[students.length][tableHeader.length];

        for (int i = 0; i < students.length; i++)
            tableData[i][0] = students[i];

        if(response.contains("["))      //if there are any grades
        {
            response = response.substring(response.indexOf("["));
            GradeCustomObject[] gradeList = gson.fromJson(response, GradeCustomObject[].class);

            for (int t = 0; t < gradeList.length; t++) {
                int j = 0;
                boolean foundTask = false;
                for (j = 0; j < tasks.length; j++)
                    if (gradeList[t].getTask().equals(tasks[j])) {
                        foundTask = true;
                        break;
                    }

                if (foundTask) {

                    for (int k = 0; k < gradeList[t].getStudent().size(); k++) {
                        int i = 0;
                        boolean foundStudent = false;
                        for (i = 0; i < students.length; i++)
                            if (gradeList[t].getStudent().get(k).equals(students[i])) {
                                foundStudent = true;
                                break;
                            }
                        if (foundStudent)
                            tableData[i][j + 1] = gradeList[t].getGrade().get(k);
                    }
                }
            }
        }

        for (int i = 0; i < students.length; i++) {
            for (int j = 0; j < tableHeader.length; j++)
                System.out.print(tableData[i][j] + " ");
            System.out.println();
        }
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
                return String.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex > 0)
                    return true;
                else return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return tableData[rowIndex][columnIndex];
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                String commandAndJson = "";
                String response = "";

                try {
                    double aux = Double.parseDouble((String) aValue);
                    if (tableData[rowIndex][columnIndex] != null) {
                        commandAndJson = "grade update ";
                    } else {
                        commandAndJson = "grade add ";
                    }
                    tableData[rowIndex][columnIndex] = aux;
                    commandAndJson += "{\"task\":\"" + tableHeader[columnIndex] + "\",\"student\":\"" + tableData[rowIndex][0] + "\",\"grade\":" + aValue + "}";
                    out.println(commandAndJson);

                    try {
                        response = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, response);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid input! Must be a number!");
                    JOptionPane.showMessageDialog(null, "Invalid input! Must be a number!");
                }

            }

            @Override
            public void addTableModelListener(TableModelListener l) {

            }

            @Override
            public void removeTableModelListener(TableModelListener l) {

            }
        };
        this.gradeTable.setModel(tableModel);
        this.gradeTable.getTableHeader().setFont(new Font("consolas", Font.BOLD, 16));
        this.gradeTable.getTableHeader().setPreferredSize(new Dimension(-1, 30));

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
