package com.courseanalytics.data;

import java.io.*;
import java.util.*;

public class CSVLoader {

    public static List<CSVRecord> load(File csvFile) throws Exception {
        List<CSVRecord> list = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        String headerLine = br.readLine();
        if (headerLine == null)
            throw new Exception("Empty file");

        // Remove BOM if present
        headerLine = headerLine.replace("\uFEFF", "");

        String[] headers = headerLine.split(",");

        for (int i = 0; i < headers.length; i++) {
            headers[i] = headers[i].trim().toLowerCase();
        }

        String[] activityExpected = {"activity_id","student_id","course_id","hours_spent","quiz_score"};
        boolean isActivity = headers.length >= 6;
        if (isActivity) {
            for (int i = 0; i < activityExpected.length; i++) {
                if (!headers[i].equals(activityExpected[i])) {
                    isActivity = false;
                    break;
                }
            }
            if (isActivity) {
                String last = headers[5];
                if (!last.equals("date") && !last.equals("datee")) {
                    isActivity = false;
                }
            }
        }

        String[] studentHeaders = {"student_id","first_name","last_name","age"};
        boolean isStudent = headers.length == studentHeaders.length;
        if (isStudent) {
            for (int i = 0; i < studentHeaders.length; i++) {
                if (!headers[i].equals(studentHeaders[i])) {
                    isStudent = false;
                    break;
                }
            }
        }

        if (!isActivity && !isStudent) {
            throw new Exception(
                    "Invalid columns.\nExpected: " + Arrays.toString(new String[]{"activity_id","student_id","course_id","hours_spent","quiz_score","datee"}) +
                            "\nFound: " + Arrays.toString(headers)
            );
        }

        if (isStudent) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length != 4) continue;

                try {
                    // For student files, create a dummy activity record to indicate student processed
                    list.add(new CSVRecord(
                            "STUDENT_" + p[0], p[0], "N/A",
                            0.0, 0.0, "STUDENT_DATA"
                    ));
                } catch (Exception e) {
                    System.out.println("Skipping bad student row: " + line);
                }
            }
            return list;
        }

        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split(",");
            if (p.length != 6) continue;

            try {
                list.add(new CSVRecord(
                        p[0], p[1], p[2],
                        Double.parseDouble(p[3]),
                        Double.parseDouble(p[4]),
                        p[5]
                ));
            } catch (Exception e) {
                System.out.println("Skipping bad row: " + line);
            }
        }
        return list;
    }
}
