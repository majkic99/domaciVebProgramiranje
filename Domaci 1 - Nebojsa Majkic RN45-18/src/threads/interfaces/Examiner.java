package threads.interfaces;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import model.ExamResult;

import threads.Student;

public abstract class Examiner implements Runnable {
	protected CountDownLatch latch;
	protected Queue<Student> studentQueue;
	protected ExecutorService studentsThreadPool;
	protected String name;
	protected Queue<ExamResult> resultList;

	public Examiner(Queue<Student> studentQueue, ExecutorService studentsThreadPool, String name,
			Queue<ExamResult> resultList, CountDownLatch latch) {
		super();
		this.studentQueue = studentQueue;
		this.studentsThreadPool = studentsThreadPool;
		this.name = name;
		this.resultList = resultList;
		this.latch = latch;
	}

	@Override
	public String toString() {
		return "Examiner [name=" + name + "]";
	}

}
