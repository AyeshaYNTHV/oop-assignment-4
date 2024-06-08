package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
        	MyForm myForm = new MyForm();
            myForm.start(primaryStage);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        connectToDatabase();
    }

    public void connectToDatabase() {
        String url = "jdbc:mysql://127.0.0.1:3306";
        String user = "root";
        String password = "123blink!";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to database successfully!");
        } catch (SQLException e) {
            System.err.println("ERROR: SQLException in " + this.getClass().getName());
            System.err.println("ERROR: " + e.getMessage());
            System.err.println("MySQL error code: " + e.getErrorCode());
            System.err.println("SQLState: " + e.getSQLState());

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Connection Error");
            alert.setHeaderText("An error occurred while connecting to the database.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
