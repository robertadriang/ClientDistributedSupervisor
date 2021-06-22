# ClientDistributedSupervisor

This repo hosts the Client from our Distributed Supervisor project. The Client has a graphical interface, implemented using Java Swing. 

## Login and Register

The Login/Register page contains:
  - _userTextField_ - input field for writing an username - **JTextField**;
  - _userTypeComboBox_ - a dropdown menu for choosing what type of account you are trying to acces - **JComboBox**;
  - _loginButton_ - a button for logging, only if the username is registered first or if the student was added to a group by a professor (more on that later) - **JButton**;
  - _registerButton_ - a button for registering - **JButton**;
  
In the **Login** constructor there are a couple of **Action Listeners**:
  - _registerButton_ 
    - when the button is clicked, the _username_ is taken from the _userTextField_ and the _userType_ is taken from _userTypeComboBox_;
    - a request is sent to the **Back-End** for saving the user into the correct database;
  - _loginButton_
    - when the button is clicked, the _username_ is taken from the _userTextField_ and the _userType_ is taken from _userTypeComboBox_;
    - a request is sent to the **Back-End** to check if the user is already in the database;
    - if the user exists, them method _login(userTypeVal)_ is called; _userTypeVal_ contains the value for the userType, so when we draw the main page, we know if the user accesing the page is student or a professor, so we can have different permissions;
  - there is another listener for the button ENTER, that does exactly the same thing as the listener for _loginButton_;
  
The constructor also calls for the _connect()_ method, for establishing a connection through a **TCP Socket**, using a standardized communication protocol described here: https://github.com/Vlad-Enia/Distributed-Supervisor/blob/master/README.md;

The method _setFrameLocation()_ makes it so that the windows will always appear in the middle of the screen.

Method _login(userTypeVal)_ will hide the login window and makes the transition to the **Main Page**, while also keeping the _socket_ and the _userTypeVal_ for future usage.
  
The **Login** page as a whole was designed using a .form GUI, while the behavoir from behind was designed through the listeners and methods above.

## Main Page

The top part of the Main Page is a** JPanel**, named _configPanel_, containing:
  - _groupComboBox_ - a JComboBox for choosing a group; when a group is selected, createTableData(group) is called, which draws the table coresponding to that group on the screen;
  - _addGroupButton_ - a **JButton** for adding a new group; when this button is pressed, a new dialog window will pop on the screen where the user can write the name of a new group to add to the the _groupComboBox_; also, a request to the **Back-End** is sent for saving the new group;
  - _addStudentButton_ - a **JButton** for adding a new student to the current group; when this button is pressed, a new dialog window will pop on the screen where the user can write the name of the a new **OR** existing student (i.e. a student that already exists in the database, but is not assigned the current group yet); also, a request to the **Back-End** is sent for saving the new student and assigning him/her to the current group; 
  - _deleteStudentButton_ - a **JButton** for removing a student from the current group; when this button is pressed, a new dialog window will pop on the screen where the user can choose from the from a dropdown menu containing the students assigned to the current group; the student will be removed from the group, but he/she, his/her tasks and grades will still be available in the database;
  - _addTaskButton_ - a **JButton** for adding a new task to the current group; when this button is pressed, a new dialog window will pop on the screen where the user can write the name of the a new **OR** existing task (i.e. a task that already exists in the database, but is not assigned the current group yet); also, a request to the **Back-End** is sent for saving the new task and assigning it to the current group; 
  - _deleteTaskButton_ - a **JButton** for removing a task from the current group; when this button is pressed, a new dialog window will pop on the screen where the user can choose from the from a dropdown menu containing the tasks assigned to the current group; the task will be removed from the group, but it will still be available in the database;

When any of the buttons above is pressed, the changes are also made to the table on the screen;
The table is fully interactive, meaning that any value for a grade can be edited from within the table cell;

All of the operations above are available for a **professor** user. If a **student** logs in, he/she can only see the tables, but can't make any modification to it.

