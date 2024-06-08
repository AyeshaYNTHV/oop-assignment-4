package application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RoomTimetable {
    private final StringProperty roomNumber;
    private final StringProperty courseName;
    private final StringProperty classDay;
    private final StringProperty startTime;
    private final StringProperty endTime;

    public RoomTimetable(String roomNumber, String courseName, String classDay, String startTime, String endTime) {
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.courseName = new SimpleStringProperty(courseName);
        this.classDay = new SimpleStringProperty(classDay);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    public StringProperty roomNumberProperty() {
        return roomNumber;
    }

    public String getCourseName() {
        return courseName.get();
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public String getClassDay() {
        return classDay.get();
    }

    public void setClassDay(String classDay) {
        this.classDay.set(classDay);
    }

    public StringProperty classDayProperty() {
        return classDay;
    }

    public String getStartTime() {
        return startTime.get();
    }

    public void setStartTime(String startTime) {
        this.startTime.set(startTime);
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public String getEndTime() {
        return endTime.get();
    }

    public void setEndTime(String endTime) {
        this.endTime.set(endTime);
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }
}
