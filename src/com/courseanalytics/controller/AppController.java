package com.courseanalytics.controller;

import com.courseanalytics.data.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class AppController {

    public boolean uploadCSV(File f) throws Exception {
        List<CSVRecord> list = CSVLoader.load(f);
        for (CSVRecord r : list) {
            if (r.date.equals("STUDENT_DATA")) {
                // Parse student_id from activity_id (format: "STUDENT_studentId")
                String studentId = r.activityId.replace("STUDENT_", "");
                // Extract actual student data from the CSV file
                List<String[]> studentData = parseStudentCSV(f);
                for (String[] student : studentData) {
                    if (student[0].equals(studentId)) {
                        StudentDAO.insertStudent(student[0], student[1], student[2], Integer.parseInt(student[3]));
                        break;
                    }
                }
            } else {
                // Insert student if not exists (using placeholder data)
                StudentDAO.insertStudent(r.studentId, "FirstName", "LastName", 20);
                // Insert activity
                ActivityDAO.insertActivity(r);
            }
        }
        return !list.isEmpty();
    }

    private List<String[]> parseStudentCSV(File f) throws Exception {
        List<String[]> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String header = br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    students.add(parts);
                }
            }
        }
        return students;
    }
}
