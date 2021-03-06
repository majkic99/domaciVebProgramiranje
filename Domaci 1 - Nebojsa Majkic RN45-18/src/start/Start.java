package start;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import enums.ExaminerType;
import model.ExamResult;
import threads.Assistant;
import threads.Professor;
import threads.Student;
import threads.interfaces.Examiner;

public class Start {
	private static final int examLengthMillis = 5000;
	private static final int defaultNumberOfParticipants = 10;
	public static Long startTimeMillis = null;

	public static void main(String[] args) {

		System.out.println("Enter the number of students that will partake in grading:");
		Scanner in = new Scanner(System.in);
		String numberOfParticipantsString = in.nextLine();
		in.close();
		int numberOfParticipants;
		try {
			numberOfParticipants = Integer.valueOf(numberOfParticipantsString);
		} catch (NumberFormatException nfe) {
			numberOfParticipants = defaultNumberOfParticipants;
		}

		Queue<Student> studentQueueForProf = new ConcurrentLinkedQueue<>();
		Queue<Student> studentQueueForAssist = new ConcurrentLinkedQueue<>();
		Queue<ExamResult> resultList = new ConcurrentLinkedQueue<>();
		ExecutorService studentsThreadPool = Executors.newScheduledThreadPool(numberOfParticipants);
		CountDownLatch latch = new CountDownLatch(2);
		Examiner prof = new Professor(studentQueueForProf, studentsThreadPool, "Milan Vidakovic", resultList, latch);
		Examiner assistant = new Assistant(studentQueueForAssist, studentsThreadPool, "Darko Dimitrijevic", resultList,
				latch);
		ExecutorService examinersThreadPool = Executors.newFixedThreadPool(2);

		/*
		  //kreiramo listu N studenata da bi pozvali sve odjednom List<Callable<Void>>
		  collectionForInvoking = new ArrayList<>(); for (int i = 1; i <=
		  numberOfParticipants; i++) { ExaminerType examiner = (Math.random() > 0.5 ?
		  ExaminerType.PROFESSOR : ExaminerType.ASSISTANT);
		  collectionForInvoking.add(toCallable( new Student(studentQueueForAssist,
		  studentQueueForProf, "Student number " + i, examiner))); }
		  System.out.println("Zavrsili smo sa kreiranjem " + numberOfParticipants +
		  " objekta tipa Student");
		  
		  try { studentsThreadPool.invokeAll(collectionForInvoking); } catch
		  (InterruptedException e1) { // TODO Auto-generated catch block
		  e1.printStackTrace(); }
		  
		  //Ovo je bolji nacin (svi studenti se odjednom startuju, ali prilikom testiranja
		  //sam stavio milion threadova i sve je otislo u vrazju mater zakomentarisano je 
		  //da se ne bi ponovilo!
		 */

		examinersThreadPool.execute(prof);
		examinersThreadPool.execute(assistant);

		while (startTimeMillis == null) {
		}

		// svaki student saznaje da li ce braniti kod profesora ili asistenta, i sam
		// pozivom run metode staje u onaj queue u koji mu je receno
		for (int i = 1; i <= numberOfParticipants; i++) {
			if (System.currentTimeMillis() - Start.startTimeMillis > examLengthMillis) {
				break;
			}
			ExaminerType examiner = (Math.random() > 0.5 ? ExaminerType.PROFESSOR : ExaminerType.ASSISTANT);
			studentsThreadPool
					.execute(new Student(studentQueueForAssist, studentQueueForProf, "Student number " + i, examiner));
		}

		while (true) {
			if (System.currentTimeMillis() - Start.startTimeMillis > examLengthMillis) {
				break;
			}
		}
		System.out.println("Proslo je 5 sekundi od pocetka termina odbrane");
		System.out.println(System.currentTimeMillis() - Start.startTimeMillis);
		examinersThreadPool.shutdownNow();
		try {
			examinersThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			System.out.println("Big error");
		}

		// ispis rezultata
		System.out.println("--------------------------");
		System.out.println("EXAM RESULTS");

		int pointsSum = 0;
		int numOfTested = 0;
		int numOfInterrupted = 0;
		for (ExamResult er : resultList) {
			System.out.println(er);
			if (er.getPoints() >= 0) {
				pointsSum += er.getPoints();
				numOfTested++;
			}else {
				numOfInterrupted++;
			}
		}
		System.out.println("Zavrsilo odbranu njih : " + numOfTested);
		System.out.println("Prekinuto na pola njih: " + numOfInterrupted);
		
		double avgPoints = (double) pointsSum / (numOfTested);
		System.out.println("Prosecan broj poena onih koji su zavrsili odbranu : " + avgPoints);

	}

	private static Callable<Void> toCallable(final Runnable runnable) {
		return new Callable<Void>() {
			@Override
			public Void call() {
				runnable.run();
				return null;
			}
		};
	}
}
