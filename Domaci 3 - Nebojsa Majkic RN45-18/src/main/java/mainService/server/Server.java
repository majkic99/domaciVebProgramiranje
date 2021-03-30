package mainService.server;

import mainService.quotes.Quote;
import mainService.quotes.quoteService.QuoteService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int TCP_PORT = 8083;

    public static void main(String[] args) {

        QuoteService quoteService = new QuoteService();
        try {
            ServerSocket ss = new ServerSocket(TCP_PORT);
            while (true) {
                Socket sock = ss.accept();
                new Thread(new ServerThread(sock, quoteService)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}