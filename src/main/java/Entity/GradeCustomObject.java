package Entity;

import java.util.ArrayList;
import java.util.List;

public class GradeCustomObject {
    String task;
    List<String> student;
    List<Double> grade;

    public GradeCustomObject() {
        student=new ArrayList<>();
        grade=new ArrayList<>();
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public List<String> getStudent() {
        return student;
    }

    public void setStudent(List<String> student) {
        this.student = student;
    }

    public List<Double> getGrade() {
        return grade;
    }

    public void setGrade(List<Double> grade) {
        this.grade = grade;
    }
}