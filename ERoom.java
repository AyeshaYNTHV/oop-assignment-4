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

public class ERoom extends Application {

    private TextField textFieldRoomNumber;
    private TextField textFieldCourseName;
    private TextField textFieldClassDay;
    private TextField textFieldStartTime;
    private TextField textFieldEndTime;
    private Button addButton;
    private Button deleteButton;
    private TableView tableViewRooms;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        textFieldRoomNumber = new TextField();
        textFieldCourseName = new TextField();
        textFieldClassDay = new TextField();
        textFieldStartTime = new TextField();
        textFieldEndTime = new TextField();

        addButton = new Button("Add Room");
        deleteButton = new Button("Delete Room");
        tableViewRooms = new TableView();

        addButton.setOnAction(e -> addRoom());
        deleteButton.setOnAction(e -> deleteRoom());

        VBox root = new VBox(10);
        root.getChildren().addAll(textFieldRoomNumber, textFieldCourseName, textFieldClassDay,
                textFieldStartTime, textFieldEndTime, addButton, deleteButton, tableViewRooms);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ERoom");
        primaryStage.show();

        loadData();
    }

    private void addRoom() {
        String roomNumber = textFieldRoomNumber.getText();
        String courseName = textFieldCourseName.getText();
        String classDay = textFieldClassDay.getText();
        String startTime = textFieldStartTime.getText();
        String endTime = textFieldEndTime.getText();

        if (roomNumber.isEmpty() || courseName.isEmpty() || classDay.isEmpty()
                || startTime.isEmpty() || endTime.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "INSERT INTO Rooms (RoomNumber) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, roomNumber);
            pstmt.executeUpdate();

            // Add entry to RoomTimetables table
            addTimetableEntry(roomNumber, courseName, classDay, startTime, endTime);

            conn.close();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Room added successfully.");
            Room.loadData(); // Refresh data in the Room form
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding room: " + e.getMessage());
        }
    }

    private void addTimetableEntry(String roomNumber, String courseName, String classDay, String startTime, String endTime) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "INSERT INTO RoomTimetables (RoomNumber, CourseName, ClassDay, StartTime, EndTime) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, roomNumber);
            pstmt.setString(2, courseName);
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

    private void deleteRoom() {
        String roomNumber = textFieldRoomNumber.getText();

        if (roomNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a room number.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "DELETE FROM Rooms WHERE RoomNumber = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, roomNumber);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Room deleted successfully.");
                // Delete entries from RoomTimetables table
                deleteTimetableEntries(roomNumber);
                Room.loadData(); // Refresh data in the Room form
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Room not found.");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting room: " + e.getMessage());
        }
    }

    private void deleteTimetableEntries(String roomNumber) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hope", "root", "123blink!");
            String query = "DELETE FROM RoomTimetables WHERE RoomNumber = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, roomNumber);
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting timetable entries: " + e.getMessage());
        }
    }

    private void loadData() {
        // Load data into the table view
        // Implement this method to load data from the RoomTimetables table into the table view
        tableViewRooms.getItems().clear(); // Clear existing data
        // Implement code here to load data into tableViewRooms from the RoomTimetables table
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
