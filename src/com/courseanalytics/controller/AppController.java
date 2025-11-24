package com.courseanalytics.controller;

import com.courseanalytics.data.*;
import java.io.File;
import java.util.List;

public class AppController {

    public boolean uploadCSV(File f) throws Exception {
        List<CSVRecord> list = CSVLoader.load(f);
        for (CSVRecord r : list) {
            // Insert student if not exists
            StudentDAO.insertStudent(r.studentId, "FirstName", "LastName", 20);
            // Insert activity
            ActivityDAO.insertActivity(r);
        }
        return !list.isEmpty();
    }
}
