package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyForm extends Application {

    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        // Set background color for the main pane
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Text title = new Text("BU H-11 TIMETABLE MANAGEMENT");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setFill(Color.DARKBLUE);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(20, 0, 20, 0));
        root.setTop(title);

        Button studentButton = createButton("Student Timetable", Color.CORNFLOWERBLUE);
        Button teacherButton = createButton("Teacher Timetable", Color.CORNFLOWERBLUE);
        Button roomButton = createButton("Rooms Timetable", Color.CORNFLOWERBLUE);
        Button courseButton = createButton("Course Timetable", Color.CORNFLOWERBLUE);
        Button eStudentButton = createButton("EStudent", Color.MEDIUMSEAGREEN);
        Button eTeacherButton = createButton("ETeacher", Color.MEDIUMSEAGREEN);
        Button eRoomButton = createButton("ERoom", Color.MEDIUMSEAGREEN);
        Button eCourseButton = createButton("ECourse", Color.MEDIUMSEAGREEN);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(studentButton, 0, 0);
        gridPane.add(teacherButton, 1, 0);
        gridPane.add(roomButton, 0, 1);
        gridPane.add(courseButton, 1, 1);
        gridPane.add(eStudentButton, 0, 2);
        gridPane.add(eTeacherButton, 1, 2);
        gridPane.add(eRoomButton, 0, 3);
        gridPane.add(eCourseButton, 1, 3);

        root.setCenter(gridPane);

        // Event handler for the studentButton
        studentButton.setOnAction(e -> {
            Student student = new Student();
            student.start(new Stage());
        });

        // Event handler for the teacherButton
        teacherButton.setOnAction(e -> {
            Teacher teacher = new Teacher();
            teacher.start(new Stage());
        });

        // Event handler for the roomButton
        roomButton.setOnAction(e -> {
            Room room = new Room();
            room.start(new Stage());
        });

        // Event handler for the courseButton
        courseButton.setOnAction(e -> {
            Course course = new Course();
            course.start(new Stage());
        });

        // Event handler for the eStudentButton
        eStudentButton.setOnAction(e -> {
            EStudent eStudent = new EStudent();
            eStudent.start(new Stage());
        });

        // Event handler for the eTeacherButton
        eTeacherButton.setOnAction(e -> {
            ETeacher eTeacher = new ETeacher();
            eTeacher.start(new Stage());
        });

        // Event handler for the eRoomButton
        eRoomButton.setOnAction(e -> {
            ERoom eRoom = new ERoom();
            eRoom.start(new Stage());
        });

        // Event handler for the eCourseButton
        eCourseButton.setOnAction(e -> {
            ECourse eCourse = new ECourse();
            eCourse.start(new Stage());
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("BU H-11 Timetable Management");
        primaryStage.show();

        connectToDatabase();
    }

    private Button createButton(String text, Color color) {
        Button button = new Button(text);
        button.setPrefWidth(150);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(5), Insets.EMPTY)));
        button.setTextFill(Color.WHITE);
        return button;
    }

    private void connectToDatabase() {
        String url = "jdbc:mysql://127.0.0.1:3306";
        String user = "root";
        String password = "123blink!";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Connection Error",
                    "An error occurred while connecting to the database.", e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showAlert(AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
