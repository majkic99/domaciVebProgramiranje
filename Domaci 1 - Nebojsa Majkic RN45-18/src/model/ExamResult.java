package model;

import threads.Student;
import threads.interfaces.Examiner;

public class ExamResult {

	private Student student;
	private Long entryTimeMillis;
	private Long startTimeMillis;
	private Long neededTimeMillis;
	private Examiner examiner;
	private int points;
	
	
	
	public ExamResult() {
		super();
		entryTimeMillis = Long.valueOf(0);
		startTimeMillis = Long.valueOf(0);
		neededTimeMillis = Long.valueOf(0);
		points = -1;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public Long getEntryTimeMillis() {
		return entryTimeMillis;
	}
	public void setEntryTimeMillis(Long entryTimeMillis) {
		this.entryTimeMillis = entryTimeMillis;
	}
	public Long getStartTimeMillis() {
		return startTimeMillis;
	}
	public void setStartTimeMillis(Long startTimeMillis) {
		this.startTimeMillis = startTimeMillis;
	}
	public Long getNeededTimeMillis() {
		return neededTimeMillis;
	}
	public void setNeededTimeMillis(Long neededTimeMillis) {
		this.neededTimeMillis = neededTimeMillis;
	}
	public Examiner getExaminer() {
		return examiner;
	}
	public void setExaminer(Examiner examiner) {
		this.examiner = examiner;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	@Override
	public String toString() {
		return "Thread: " + this.student.getName() + " Arrival: " + this.getEntryTimeMillis() + "ms " + this.examiner.toString() + 
				" TTC: " + this.getNeededTimeMillis() + "ms:" + this.getStartTimeMillis() + "ms Score: " + this.getPoints();
	}

	
	

}
