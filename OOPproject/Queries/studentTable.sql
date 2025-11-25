CREATE TABLE students(
	student_id VARCHAR(20) PRIMARY KEY,
	first_name VARCHAR(20),
	last_name VARCHAR(20),
	age INT

);

CREATE TABLE studentactivity(
	activity_id VARCHAR(7) ,
    student_id VARCHAR(4), 
	course_id VARCHAR(4),
	hours_spent DOUBLE,
	quiz_score DOUBLE,
	datee DATE,
    FOREIGN KEY(student_id) REFERENCES students(student_id)

);

SELECT * FROM studentactivity;
SELECT * FROM students;
