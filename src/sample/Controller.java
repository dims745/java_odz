package sample;

import Models.Task;
import database.DatabaseCon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javax.swing.*;
import java.sql.Time;

public class Controller {
    private DatabaseCon connection;

    private final ObservableList<Task> taskList = FXCollections.observableArrayList();

    @FXML
    private Label timer;

    private Timer timerCalculate;

    private long time = 0;

    private boolean pause = true;

    private long lastSystemTime;

    @FXML
    private TableView<Task> table;

    @FXML
    private TextField name;

    @FXML
    private TextField comment;

    @FXML
    private void initialize() throws Exception {
        connection = new DatabaseCon(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/odz_java?useSSL=true",
                "root",
                "root"
        );
        TableColumn<Task, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Task, String> colComment = new TableColumn<>("Comment");
        colComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        colComment.setMinWidth(100);

        TableColumn<Task, String> colTime = new TableColumn<>("Time");
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Task, Void> cellButton = addButtonToTable();

        cellButton.setMinWidth(15);

        table.getColumns().addAll(colName, colComment, colTime, cellButton);

        if(connection.isError()) {
            timer.setText("Error connection");
        } else {
            updateTasks();
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            table.setItems(taskList);
        }
    }

    public void updateTasks() throws Exception {
        taskList.clear();
        taskList.addAll(connection.getTasksList());
    }

    @FXML
    private Button timerButton;
    @FXML
    private void timerButtonAction() {
        if (timerCalculate == null) {
            timerCalculate = new Timer( 200, e -> {
                long currentSystemTime = System.currentTimeMillis();
                if (!pause) {
                    if (time != lastSystemTime) {
                        time += currentSystemTime - lastSystemTime;
                        lastSystemTime = currentSystemTime;
                        Platform.runLater(() -> timer.setText(new Time(time).toString()));
                    }
                } else {
                    lastSystemTime = currentSystemTime;
                }
            });
            lastSystemTime = System.currentTimeMillis();
            time = 0;
            pause = false;
            timerCalculate.start();
        } else {
            pause = !pause;
        }

        if (pause) {
            timerButton.setText("Start");
        } else {
            timerButton.setText("Pause");
        }
    }

    @FXML
    private void addNewTask() throws Exception {
        Task newTask = new Task(name.getText(), comment.getText(), new Time(time));
        connection.addTask(newTask);
        updateTasks();
        clear();
    }

    @FXML
    private void clear() {
        name.clear();
        comment.clear();
        time = 0;
        pause = true;
        timerCalculate = null;
        timer.setText("00:00:00");
        timerButton.setText("Start");
    }

    private TableColumn<Task, Void> addButtonToTable() {
        TableColumn<Task, Void> colBtn = new TableColumn("");

        Callback<TableColumn<Task, Void>, TableCell<Task, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Task, Void> call(final TableColumn<Task, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button("Remove");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Task task = getTableView().getItems().get(getIndex());
                            try {
                                connection.deleteTask(task);
                                updateTasks();
                            } catch (Exception throwables) {
                                throwables.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colBtn.setCellFactory(cellFactory);

        return colBtn;

    }
}
