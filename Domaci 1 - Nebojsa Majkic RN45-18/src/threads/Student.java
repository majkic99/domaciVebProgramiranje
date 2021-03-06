package threads;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import enums.ExaminerType;
import model.ExamResult;
import start.Start;
import threads.interfaces.Examiner;

public class Student implements Callable<Long>, Runnable {
	private Queue<Student> studentQueueProf;
	private Queue<Student> studentQueueAssist;
	private ExamResult examResult;
	private ExaminerType examinerType;
	private CountDownLatch latch;

	private String name;

	@Override
	public void run() {
		try {
			//odspavamo neko vreme izmedju 0 i 1s
			long sleepTime = (long) (Math.random() * 1000);
			Thread.currentThread().sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//sada smo dosli na termin odbrane, saznajemo da li smo kod profesora ili asistenta i stajemo u pravi red
		switch (examinerType) {
		case ASSISTANT:
			studentQueueAssist.add(this);
			break;
		case PROFESSOR:
			studentQueueProf.add(this);
			break;
		}
		
		examResult.setEntryTimeMillis(System.currentTimeMillis() - Start.startTimeMillis);

	}

	@Override
	public Long call() throws Exception {
		//saceka da latch prodje, default =1 , profesor moze da setuje na 2
		latch.countDown();
		//nakon toga pisemo kad je startovao
		getExamResult().setStartTimeMillis(System.currentTimeMillis() - Start.startTimeMillis);
		long workTime = (long) (500 + (Math.random() * 500));
		//odspavamo random vreme izmedju 0.5 i 1
		Thread.currentThread().sleep(workTime);
		getExamResult().setNeededTimeMillis(workTime);
		Random r = new Random();
		int result = r.nextInt(10 - 0 + 1 ) + 0;
		//vratimo osvojeni broj poena
		return Long.valueOf(result);

	}

	public Student(Queue<Student> studentQueueAssist, Queue<Student> studentQueueProf, String name,
			ExaminerType examinerType) {
		super();
		this.studentQueueAssist = studentQueueAssist;
		this.studentQueueProf = studentQueueProf;
		this.name = name;
		examResult = new ExamResult();
		examResult.setStudent(this);
		this.examinerType = examinerType;
		this.latch = new CountDownLatch(1);
	}

	@Override
	public String toString() {
		return "Student [name=" + name + "]";
	}

	public ExamResult getExamResult() {
		return examResult;
	}

	public void setExamResult(ExamResult examResult) {
		this.examResult = examResult;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	
}
