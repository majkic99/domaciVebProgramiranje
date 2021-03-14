package client;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientMain {
	
	public static final int numberOfPlayers = 105;
	
    public static void main(String[] args) throws IOException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(numberOfPlayers);

        for (int i = 0; i < numberOfPlayers; i++) {
        	int pause = (new Random()).nextInt(1000);
            scheduledExecutorService.schedule(new ClientThread(), pause, TimeUnit.MILLISECONDS);
        }

        scheduledExecutorService.shutdown();
    }
}
