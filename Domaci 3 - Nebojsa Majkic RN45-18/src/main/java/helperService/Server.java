package helperService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static final int PORT = 8084;

    public static void main(String[] args) {

        try {

            ExecutorService executorService = Executors.newCachedThreadPool();
            ServerSocket ss = new ServerSocket(PORT);

            while (true) {
                Socket sock = ss.accept();
                executorService.submit(new ServerThread(sock));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
