CREATE TABLE student(
	student_id VARCHAR(20) PRIMARY KEY,
	first_name VARCHAR(20),
	last_name VARCHAR(20),
	age INT

);

CREATE TABLE studentActivity(
	activity_id VARCHAR(7) ,
    student_id VARCHAR(4), 
	course_id VARCHAR(4),
	hours_spent INT,
	quiz_score INT,
	datee DATE,
    FOREIGN KEY(student_id) REFERENCES student(student_id)

);

SELECT * FROM StudentActivity;
SELECT * FROM Student;
