
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String serverAddress = "127.0.0.1"; // The server's IP address
        int PORT = 8101; // The server's port
        try (
                Socket socket = new Socket(serverAddress, PORT);
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))) {
            // Send a request to the server
            Scanner keyboard = new Scanner(System.in);
            String request = "";
            while (request.compareTo("exit") != 0) {
                System.out.print("\nCommand: ");
                request = keyboard.nextLine();
                if (request.startsWith("register")) {
                    /* Register command */
                    /* Register as prof= register1
                     *  Register as student= register2*/
                    String commandAndJSON = "";
                    if (request.startsWith("register1")) {
                        commandAndJSON += "register1 ";
                        commandAndJSON += "{\"username\":\"" + request.substring(10) + "\"}";
                    } else {
                        commandAndJSON += "register2 ";
                        commandAndJSON += "{\"username\":\"" + request.substring(10) + "\"}";
                    }
                    out.println(commandAndJSON);
                }
                else if(request.startsWith("grade")){
                    String comandAndJSON="grade ";
                    /* grade command
                     grade add [Task] [Student] [Grade]
                     */
                    if(request.startsWith("grade add")){
                        comandAndJSON+="add ";
                        var auxSplit=request.split("\"");
                        String taskName=auxSplit[auxSplit.length-5];
                        String studentName=auxSplit[auxSplit.length-3];
                        String grade=auxSplit[auxSplit.length-1];
                        comandAndJSON+="{\"task\":\""+taskName+"\",\"student\":\""+studentName+"\",\"grade\":"+grade+"}";
                    }
                    else if (request.startsWith("grade get")){
                        comandAndJSON+="get ";
                        if(request.startsWith("grade get all")){
                            comandAndJSON+="all ";
                            comandAndJSON+="{\"name\":\""+request.substring(14)+"\"}";
                        }
                    }
                    else if(request.startsWith("grade update")){
                        comandAndJSON+="update ";
                        var auxSplit=request.split("\"");
                        String taskName=auxSplit[auxSplit.length-5];
                        String studentName=auxSplit[auxSplit.length-3];
                        String grade=auxSplit[auxSplit.length-1];
                        comandAndJSON+="{\"task\":\""+taskName+"\",\"student\":\""+studentName+"\",\"grade\":"+grade+"}";
                    }
                    out.println(comandAndJSON);
                }
                else if(request.startsWith("group-student")){
                    /** group-student command */
                    /* Add a student to a group */
                    /** group-professor command */
                    /* Add a task to a group */
                    String commandAndJSON="group-student ";
                    if(request.startsWith("group-student add")){
                        commandAndJSON+="add ";
                        var auxSplit=request.split("\"");
                        String groupName=auxSplit[auxSplit.length-3];
                        String studentName=auxSplit[auxSplit.length-1];
                        commandAndJSON+="{\"groupname\":\""+groupName+"\",\"student\":\""+studentName+"\"}";
                    }
                    else if(request.startsWith("group-student get")){
                        commandAndJSON+="get ";
                        if(request.startsWith("group-student get all")){
                            commandAndJSON+="all ";
                            commandAndJSON+="{\"name\":\""+request.substring(22)+"\"}";
                        }
                    }
                    else if(request.startsWith("group-student delete")){
                        commandAndJSON+="delete ";
                        var auxSplit=request.split("\"");
                        String groupName=auxSplit[auxSplit.length-3];
                        String studentName=auxSplit[auxSplit.length-1];
                        commandAndJSON+="{\"groupname\":\""+groupName+"\",\"student\":\""+studentName+"\"}";
                    }
                    out.println(commandAndJSON);
                }
                else if (request.startsWith("group-professor")){
                    /** group-professor command */
                    /* Add a task to a group */
                    String commandAndJSON="group-professor ";
                    if(request.startsWith("group-professor add")){
                        commandAndJSON+="add ";
                        var auxSplit=request.split("\"");
                        String groupName=auxSplit[auxSplit.length-3];
                        String professorName=auxSplit[auxSplit.length-1];
                        commandAndJSON+="{\"groupname\":\""+groupName+"\",\"professor\":\""+professorName+"\"}";
                    }
                    out.println(commandAndJSON);
                }
                else if (request.startsWith("group-task")){
                    /** group-task command*/
                    /* Add a task to a group */
                    String commandAndJSON="group-task ";
                    if(request.startsWith("group-task add")){
                        commandAndJSON+="add ";
                        var auxSplit=request.split("\"");
                        String groupName=auxSplit[auxSplit.length-3];
                        String taskName=auxSplit[auxSplit.length-1];
                        commandAndJSON+="{\"groupname\":\""+groupName+"\",\"task\":\""+taskName+"\"}";
                    }
                    else if(request.startsWith("group-task get")){
                        commandAndJSON+="get ";
                        if(request.startsWith("group-task get all")){
                            commandAndJSON+="all ";
                            commandAndJSON+="{\"name\":\""+request.substring(19)+"\"}";
                        }
                    }
                    out.println(commandAndJSON);
                }
                else if (request.startsWith("login")) {
                    /* Login command */
                    /* Login as prof= login1
                     *  Login as student= login2*/
                    String commandAndJSON = "";
                    if (request.startsWith("login1")) {
                        commandAndJSON = "login1 ";
                        commandAndJSON += "{\"username\":\"" + request.substring(7) + "\"}";
                    }
                    if (request.startsWith("login2")) {
                        commandAndJSON = "login2 ";
                        commandAndJSON += "{\"username\":\"" + request.substring(7) + "\"}";
                    }

                    out.println(commandAndJSON);
                }
                else if (request.startsWith("task")) {
                    String commandAndJSON = "task ";
                    /* Task command
                     *  task add [string] = add a task with the string as a name*/
                    if (request.startsWith("task add")) {
                        commandAndJSON += "add ";
                        commandAndJSON += "{\"name\":\"" + request.substring(9) + "\"}";
                    }
                    out.println(commandAndJSON);
                }
                else if (request.startsWith("group")) {
                    String commandAndJSON = "group ";
                    /*
                    group command
                    group add [string] = add a group with the string as a name*/
                    if (request.startsWith("group add")) {
                        commandAndJSON += "add ";
                        commandAndJSON += "{\"name\":\"" + request.substring(10) + "\"}";
                    }
                    else if(request.startsWith("group get ")){
                        commandAndJSON+="get ";
                        if(request.startsWith("group get all")){
                            commandAndJSON+="all ";
                        }
                    }
                    out.println(commandAndJSON);
                }
                else {
                    out.println(request);
                }
                // Wait the response from the server ("Hello World!")
                String response = in.readLine();
                if (!request.equals("exit"))
                    System.out.println(response.replaceAll("\\|", "\n") + "\n");
            }
            System.out.println("Exiting the client...");

        } catch (UnknownHostException e) {
            System.err.println("No server listening... " + e);
        } catch (SocketException e) {
            System.out.printf("Socket exception. Probably timeout LOL");
        }
    }
}