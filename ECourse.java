package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ECourse extends Application {

    private TextField textFieldCourseName;
    private TextField textFieldRoomNumber;
    private TextField textFieldClassDay;
    private TextField textFieldStartTime;
    private TextField textFieldEndTime;
    private Button addButton;
    private Button deleteButton;
    private TableView tableViewCourses;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        textFieldCourseName = new TextField();
        textFieldRoomNumber = new TextField();
        textFieldClassDay = new TextField();
        textFieldStartTime = new TextField();
        textFieldEndTime = new TextField();

        addButton = new Button("Add Course");
        deleteButton = new Button("Delete Course");
        tableViewCourses = new TableView();

        addButton.setOnAction(e -> addCourse());
        deleteButton.setOnAction(e -> deleteCourse());

        VBox root = new VBox(10);
        root.getChildren().addAll(textFieldCourseName, textFieldRoomNumber, textFieldClassDay,
                textFieldStartTime, textFieldEndTime, addButton, deleteButton, tableViewCourses);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ECourse");
        primaryStage.show();

        loadData();
    }

    private void addCourse() {
        String courseName = textFieldCourseName.getText();
        String roomNumber = textFieldRoomNumber.getText();
        String classDay = textFieldClassDay.getText();
        String startTime = textFieldStartTime.getText();
        String endTime = textFieldEndTime.getText();

        if (courseName.isEmpty() || roomNumber.isEmpty() || classDay.isEmpty()
                || startTime.isEmpty() || endTime.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "INSERT INTO Courses (CourseName) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, courseName);
            pstmt.executeUpdate();

            // Add entry to CourseTimetables table
            addTimetableEntry(courseName, roomNumber, classDay, startTime, endTime);

            conn.close();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Course added successfully.");
            loadData(); // Refresh data in the table view
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding course: " + e.getMessage());
        }
    }

    private void addTimetableEntry(String courseName, String roomNumber, String classDay, String startTime, String endTime) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "INSERT INTO CourseTimetables (CourseName, RoomNumber, ClassDay, StartTime, EndTime) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, courseName);
            pstmt.setString(2, roomNumber);
            pstmt.setString(3, classDay);
            pstmt.setString(4, startTime);
            pstmt.setString(5, endTime);
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding timetable entry: " + e.getMessage());
        }
    }

    private void deleteCourse() {
        String courseName = textFieldCourseName.getText();

        if (courseName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a course name.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            // First, delete entries from CourseTimetables table
            deleteTimetableEntries(courseName, conn);

            // Then, delete the course from Courses table
            String query = "DELETE FROM Courses WHERE CourseName = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, courseName);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Course deleted successfully.");
                loadData(); // Refresh data in the table view
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Course not found.");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting course: " + e.getMessage());
        }
    }

    private void deleteTimetableEntries(String courseName, Connection conn) {
        try {
            String query = "DELETE FROM CourseTimetables WHERE CourseName = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, courseName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting timetable entries: " + e.getMessage());
        }
    }


    private void loadData() {
        // Load data into the table view
        // Implement this method to load data from the CourseTimetables table into the table view
        tableViewCourses.getItems().clear(); // Clear existing data
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "SELECT * FROM CourseTimetables";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Assuming CourseTimetable is a class that holds the timetable data
                CourseTimetable course = new CourseTimetable(
                        rs.getString("CourseName"),
                        rs.getString("RoomNumber"),
                        rs.getString("ClassDay"),
                        rs.getString("StartTime"),
                        rs.getString("EndTime")
                );
                tableViewCourses.getItems().add(course);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading data: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

