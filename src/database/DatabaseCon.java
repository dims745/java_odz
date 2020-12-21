package database;

import Models.Task;
import java.sql.*;
import java.util.List ;
import java.util.ArrayList ;

public class DatabaseCon {

    private boolean isError = false;

    public boolean isError() {
        return isError;
    }

    private Connection connection ;

    public DatabaseCon(String driverClassName, String dbURL, String user, String password) {
        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(dbURL, user, password);
        } catch (Exception e) {
            isError = true;
        }
    }

    public List<Task> getTasksList() throws SQLException {
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from tasks");
        ){
            List<Task> taskList = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String vendor = rs.getString("name");
                String name = rs.getString("comment");
                Time date = rs.getTime("time");
                Task person = new Task(id, vendor, name, date);
                taskList.add(person);
            }
            return taskList;
        }
    }

    public void addTask(Task task) throws SQLException {
        Statement stmnt = connection.createStatement();
        stmnt.execute("INSERT INTO `tasks` (`name`, `comment`, `time`) VALUES ('"
                .concat(task.getName())
                .concat("', '")
                .concat(task.getComment())
                .concat("', '")
                .concat(task.getTime().toString())
                .concat("');")
        );
    }

    public void deleteTask(Task task) throws SQLException {
        Statement stmnt = connection.createStatement();
        String id = String.valueOf(task.getId());
        stmnt.execute("DELETE FROM `tasks` WHERE `tasks`.`id` = ".concat(id));
    }
}
