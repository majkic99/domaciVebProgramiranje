package threads;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import model.ExamResult;
import start.Start;
import threads.interfaces.Examiner;

public class Assistant extends Examiner {

	public Assistant(Queue<Student> studentQueue, ExecutorService studentsThreadPool, String name,
			Queue<ExamResult> resultList, CountDownLatch latch) {
		super(studentQueue, studentsThreadPool, "Assistant: " + name, resultList, latch);
	}

	@Override
	public void run() {
		latch.countDown();
		Start.startTimeMillis = System.currentTimeMillis();
		
		while (!Thread.currentThread().isInterrupted()) {
			try {
				//sve dok nije interuptovan prozvace sledeceg iz reda, sacekace da on zavrsi svoju odbranu i dodeliti mu poene
				if (studentQueue.peek() != null) {
					Student student = studentQueue.poll();
					student.getExamResult().setExaminer(this);
					resultList.add(student.getExamResult());
					FutureTask<Long> task = new FutureTask<>(student);
					studentsThreadPool.execute(task);
					Long points = task.get();
					
					student.getExamResult().setPoints(points.intValue());
				}

			} catch (InterruptedException e) {
				System.out.println("Interuptovan sam " + (System.currentTimeMillis() - Start.startTimeMillis));
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

}
