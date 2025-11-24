package com.courseanalytics.data;

public class CSVRecord {
    public String activityId;
    public String studentId;
    public String courseId;
    public double hoursSpent;
    public double quizScore;
    public String date;

    public CSVRecord(String activityId, String studentId,
                     String courseId, double hoursSpent,
                     double quizScore, String date) {
        this.activityId = activityId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.hoursSpent = hoursSpent;
        this.quizScore = quizScore;
        this.date = date;
    }
}
