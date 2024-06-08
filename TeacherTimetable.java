package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TeacherTimetable {
    private final StringProperty teacherName;
    private final StringProperty courseName;
    private final StringProperty roomNumber;
    private final StringProperty classDay;
    private final StringProperty startTime;
    private final StringProperty endTime;

    public TeacherTimetable(String teacherName, String courseName, String roomNumber, String classDay, String startTime, String endTime) {
        this.teacherName = new SimpleStringProperty(teacherName);
        this.courseName = new SimpleStringProperty(courseName);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.classDay = new SimpleStringProperty(classDay);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
    }

    public String getTeacherName() {
        return teacherName.get();
    }

    public StringProperty teacherNameProperty() {
        return teacherName;
    }

    public String getCourseName() {
        return courseName.get();
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public StringProperty roomNumberProperty() {
        return roomNumber;
    }

    public String getClassDay() {
        return classDay.get();
    }

    public StringProperty classDayProperty() {
        return classDay;
    }

    public String getStartTime() {
        return startTime.get();
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public String getEndTime() {
        return endTime.get();
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }
}
