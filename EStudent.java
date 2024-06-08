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

public class EStudent extends Application {

    private TextField textFieldStudentName;
    private TextField textFieldCourseName;
    private TextField textFieldRoomNumber;
    private TextField textFieldClassDay;
    private TextField textFieldStartTime;
    private TextField textFieldEndTime;
    private Button addButton;
    private Button deleteButton;
    private TableView tableViewStudents;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        textFieldStudentName = new TextField();
        textFieldCourseName = new TextField();
        textFieldRoomNumber = new TextField();
        textFieldClassDay = new TextField();
        textFieldStartTime = new TextField();
        textFieldEndTime = new TextField();

        addButton = new Button("Add Student");
        deleteButton = new Button("Delete Student");
        tableViewStudents = new TableView();

        addButton.setOnAction(e -> addStudent());
        deleteButton.setOnAction(e -> deleteStudent());

        VBox root = new VBox(10);
        root.getChildren().addAll(textFieldStudentName, textFieldCourseName, textFieldRoomNumber, textFieldClassDay,
                textFieldStartTime, textFieldEndTime, addButton, deleteButton, tableViewStudents);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EStudent");
        primaryStage.show();

        loadData();
    }

    private void addStudent() {
        String studentName = textFieldStudentName.getText();
        String courseName = textFieldCourseName.getText();
        String roomNumber = textFieldRoomNumber.getText();
        String classDay = textFieldClassDay.getText();
        String startTime = textFieldStartTime.getText();
        String endTime = textFieldEndTime.getText();

        if (studentName.isEmpty() || courseName.isEmpty() || roomNumber.isEmpty() || classDay.isEmpty()
                || startTime.isEmpty() || endTime.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "INSERT INTO Students (sName) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, studentName);
            pstmt.executeUpdate();

            // Add entry to Timetables table
            addTimetableEntry(studentName, courseName, roomNumber, classDay, startTime, endTime);

            conn.close();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student added successfully.");
            loadData(); // Refresh data in the table view
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding student: " + e.getMessage());
        }
    }

    private void addTimetableEntry(String studentName, String courseName, String roomNumber, String classDay, String startTime, String endTime) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "INSERT INTO Timetables (StudentName, CourseName, RoomNumber, ClassDay, StartTime, EndTime) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, studentName);
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

    private void deleteStudent() {
        String studentName = textFieldStudentName.getText();

        if (studentName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a student name.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "DELETE FROM Students WHERE sName = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, studentName);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Student deleted successfully.");
                // Delete entries from Timetables table
                deleteTimetableEntries(studentName);
                loadData(); // Refresh data in the table view
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Student not found.");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting student: " + e.getMessage());
        }
    }

    private void deleteTimetableEntries(String studentName) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "DELETE FROM Timetables WHERE StudentName = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, studentName);
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting timetable entries: " + e.getMessage());
        }
    }

    private void loadData() {
        // Load data into the table view
        // Implement this method to load data from the Students table into the table view
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
