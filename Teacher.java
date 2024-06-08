package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;

public class Teacher extends Application {
    private TableView<TeacherTimetable> table;
    private TextField searchField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Teacher Timetable");

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 400);

        Label title = new Label("Teacher Timetable");
        title.setFont(new Font("Arial", 24));
        root.setTop(title);
        BorderPane.setMargin(title, new Insets(10, 10, 10, 10));

        table = new TableView<>();
        TableColumn<TeacherTimetable, String> teacherNameCol = new TableColumn<>("Teacher Name");
        teacherNameCol.setCellValueFactory(cellData -> cellData.getValue().teacherNameProperty());

        TableColumn<TeacherTimetable, String> courseNameCol = new TableColumn<>("Course Name");
        courseNameCol.setCellValueFactory(cellData -> cellData.getValue().courseNameProperty());

        TableColumn<TeacherTimetable, String> roomNumberCol = new TableColumn<>("Room Number");
        roomNumberCol.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty());

        TableColumn<TeacherTimetable, String> classDayCol = new TableColumn<>("Class Day");
        classDayCol.setCellValueFactory(cellData -> cellData.getValue().classDayProperty());

        TableColumn<TeacherTimetable, String> startTimeCol = new TableColumn<>("Start Time");
        startTimeCol.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());

        TableColumn<TeacherTimetable, String> endTimeCol = new TableColumn<>("End Time");
        endTimeCol.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());

        table.getColumns().addAll(teacherNameCol, courseNameCol, roomNumberCol, classDayCol, startTimeCol, endTimeCol);

        root.setCenter(table);

        GridPane searchPane = new GridPane();
        searchPane.setPadding(new Insets(10));
        searchPane.setHgap(10);
        searchPane.setVgap(10);

        Label searchLabel = new Label("Teacher Name:");
        searchPane.add(searchLabel, 0, 0);

        searchField = new TextField();
        searchPane.add(searchField, 1, 0);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchTimetable());
        searchPane.add(searchButton, 2, 0);

        root.setBottom(searchPane);

        primaryStage.setScene(scene);
        primaryStage.show();

        loadData();
    }

    private void loadData() {
        table.getItems().clear();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "SELECT * FROM TeacherTimetables";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                TeacherTimetable timetable = new TeacherTimetable(
                        rs.getString("TeacherName"),
                        rs.getString("CourseName"),
                        rs.getString("RoomNumber"),
                        rs.getString("ClassDay"),
                        rs.getString("StartTime"),
                        rs.getString("EndTime")
                );
                table.getItems().add(timetable);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchTimetable() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a teacher name for search.");
            alert.showAndWait();
            return;
        }

        table.getItems().clear();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "SELECT * FROM TeacherTimetables WHERE TeacherName = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, searchText);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TeacherTimetable timetable = new TeacherTimetable(
                        rs.getString("TeacherName"),
                        rs.getString("CourseName"),
                        rs.getString("RoomNumber"),
                        rs.getString("ClassDay"),
                        rs.getString("StartTime"),
                        rs.getString("EndTime")
                );
                table.getItems().add(timetable);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
