package threads;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import model.ExamResult;
import start.Start;
import threads.interfaces.Examiner;

public class Professor extends Examiner {

	public Professor(Queue<Student> studentQueue, ExecutorService studentsThreadPool, String name,
			Queue<ExamResult> resultList, CountDownLatch latch) {
		super(studentQueue, studentsThreadPool, "Professor: " + name, resultList, latch);
	}

	@Override
	public void run() {
		latch.countDown();
		Start.startTimeMillis = System.currentTimeMillis();
		
		while (!Thread.currentThread().isInterrupted()) {
			try {
				//vrtece se u while petlji sve do trenutka dok u redu ne bude bilo 2 ili vise coveka
				//ako je samo jedan covek u redu kod profesora on nece biti primljen
				if (studentQueue.size() >= 2) {
					Student firstStudent = studentQueue.poll();
					Student secondStudent = studentQueue.poll();

					firstStudent.getExamResult().setExaminer(this);
					secondStudent.getExamResult().setExaminer(this);
					//menjamo default latch studenta (1), sada ce morati oba da pocnu u isto vreme
					CountDownLatch studentLatch = new CountDownLatch(2);
					firstStudent.setLatch(studentLatch);
					secondStudent.setLatch(studentLatch);
					resultList.add(firstStudent.getExamResult());
					resultList.add(secondStudent.getExamResult());
					//cim smo zapoceli odbranu studenata ubacujemo u listu rezultata (ako bude interuptovano i ne zavrse na vreme nece imati poene
					
					FutureTask<Long> task1 = new FutureTask<>(firstStudent);
					FutureTask<Long> task2 = new FutureTask<>(secondStudent);
					studentsThreadPool.execute(task1);
					studentsThreadPool.execute(task2);
					Long points1 = task1.get();
					Long points2 = task2.get();
					//pozivamo call metode oba studenata, svaki student ce spavati odredjeno vreme
					//nastavljamo se izvrsavanjem cim se probudi onaj koji je duze spavao (oba su budni)
					
					firstStudent.getExamResult().setPoints(points1.intValue());
					secondStudent.getExamResult().setPoints(points2.intValue());
					
				}

			} catch (InterruptedException e) {
				System.out.println("Interuptovan sam " + (System.currentTimeMillis() - Start.startTimeMillis));
				Thread.currentThread().interrupt();
				
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
