package helperService;

import com.google.gson.Gson;
import mainService.quotes.Quote;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ServerThread extends Thread {

    Socket socket;
    BufferedReader in;
    PrintWriter out;

    Gson gson;

    public ServerThread(Socket socket) {
        this.socket = socket;

        this.gson = new Gson();

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        int numberOfQuotes = receiveRequest();
        Random r = new Random();
        sendResponse(r.nextInt(numberOfQuotes));

        try {
            socket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Integer receiveRequest() {

        Integer request = null;
        try {
            request = gson.fromJson(in.readLine(), Integer.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return request;
    }
    public void sendResponse(Integer response) {
        out.println(gson.toJson(response));
    }
}
