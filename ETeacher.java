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
import java.sql.SQLException;

public class ETeacher extends Application {

    private TextField textFieldTeacherName;
    private TextField textFieldCourseName;
    private TextField textFieldRoomNumber;
    private TextField textFieldClassDay;
    private TextField textFieldStartTime;
    private TextField textFieldEndTime;
    private Button addButton;
    private Button deleteButton;
    private TableView tableViewTeachers;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        textFieldTeacherName = new TextField();
        textFieldCourseName = new TextField();
        textFieldRoomNumber = new TextField();
        textFieldClassDay = new TextField();
        textFieldStartTime = new TextField();
        textFieldEndTime = new TextField();

        addButton = new Button("Add Teacher");
        deleteButton = new Button("Delete Teacher");
        tableViewTeachers = new TableView();

        addButton.setOnAction(e -> addTeacher());
        deleteButton.setOnAction(e -> deleteTeacher());

        VBox root = new VBox(10);
        root.getChildren().addAll(textFieldTeacherName, textFieldCourseName, textFieldRoomNumber, textFieldClassDay,
                textFieldStartTime, textFieldEndTime, addButton, deleteButton, tableViewTeachers);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ETeacher");
        primaryStage.show();

        loadData();
    }

    private void addTeacher() {
        String teacherName = textFieldTeacherName.getText();
        String courseName = textFieldCourseName.getText();
        String roomNumber = textFieldRoomNumber.getText();
        String classDay = textFieldClassDay.getText();
        String startTime = textFieldStartTime.getText();
        String endTime = textFieldEndTime.getText();

        if (teacherName.isEmpty() || courseName.isEmpty() || roomNumber.isEmpty() || classDay.isEmpty()
                || startTime.isEmpty() || endTime.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "INSERT INTO Teachers (tName) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, teacherName);
            pstmt.executeUpdate();

            // Add entry to TeacherTimetables table
            addTimetableEntry(teacherName, courseName, roomNumber, classDay, startTime, endTime);

            conn.close();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Teacher added successfully.");
            loadData(); // Refresh data in the table view
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding teacher: " + e.getMessage());
        }
    }

    private void addTimetableEntry(String teacherName, String courseName, String roomNumber, String classDay, String startTime, String endTime) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "INSERT INTO TeacherTimetables (TeacherName, CourseName, RoomNumber, ClassDay, StartTime, EndTime) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, teacherName);
            pstmt.setString(2, courseName);
            pstmt.setString(3, roomNumber);
            pstmt.setString(4, classDay);
            pstmt.setString(5, startTime);
            pstmt.setString(6, endTime);
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding timetable entry: " + e.getMessage());
        }
    }

    private void deleteTeacher() {
        String teacherName = textFieldTeacherName.getText();

        if (teacherName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a teacher name.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");

            // First, delete entries from TeacherTimetables table
            String deleteTimetableQuery = "DELETE FROM TeacherTimetables WHERE TeacherName = ?";
            PreparedStatement deleteTimetableStmt = conn.prepareStatement(deleteTimetableQuery);
            deleteTimetableStmt.setString(1, teacherName);
            deleteTimetableStmt.executeUpdate();

            // Then, delete the teacher
            String deleteTeacherQuery = "DELETE FROM Teachers WHERE tName = ?";
            PreparedStatement deleteTeacherStmt = conn.prepareStatement(deleteTeacherQuery);
            deleteTeacherStmt.setString(1, teacherName);
            int rowsAffected = deleteTeacherStmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Teacher deleted successfully.");
                loadData(); // Refresh data in the table view
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Teacher not found.");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting teacher: " + e.getMessage());
        }
    }

    private void deleteTimetableEntries(String teacherName) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "DELETE FROM TeacherTimetables WHERE TeacherName = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, teacherName);
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting timetable entries: " + e.getMessage());
        }
    }

    private void loadData() {
        // Load data into the table view
        // Implement this method to load data from the Teachers table into the table view
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
