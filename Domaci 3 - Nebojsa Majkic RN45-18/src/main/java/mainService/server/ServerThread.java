package mainService.server;

import com.google.gson.Gson;
import mainService.http.HttpMethod;
import mainService.http.response.HtmlResponse;
import mainService.http.response.RedirectResponse;
import mainService.quotes.Quote;
import mainService.quotes.quoteService.QuoteService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerThread implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson;
    private QuoteService quoteService;

    public ServerThread(Socket sock, QuoteService quoteService) {
        this.client = sock;
        this.gson = new Gson();
        this.quoteService = quoteService;
        try {
            //inicijalizacija ulaznog toka
            in = new BufferedReader(
                    new InputStreamReader(
                            client.getInputStream()));

            //inicijalizacija izlaznog sistema
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    client.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            // uzimamo samo prvu liniju zahteva, iz koje dobijamo HTTP method i putanju

            String requestLine = in.readLine();

            StringTokenizer stringTokenizer = new StringTokenizer(requestLine);

            String method = stringTokenizer.nextToken();
            String path = stringTokenizer.nextToken();

            System.out.println("\nHTTP ZAHTEV KLIJENTA:\n");
            do {
                System.out.println(requestLine);
                requestLine = in.readLine();
            } while (!requestLine.trim().equals(""));


            if (method.equals(HttpMethod.GET.toString()) && path.equals("/quotes")){
                sendToHomePage(out, false);
            }else if (method.equals(HttpMethod.POST.toString())&& path.equals("/save-quote")){
                StringBuilder payload = new StringBuilder();
                while(in.ready()){
                    payload.append((char) in.read());
                }
                System.out.println("Payload data is: "+payload.toString());

                String[] fullText = payload.toString().split("&");
                String author = fullText[0].substring(7).replaceAll("\\+", " ");
                String text = fullText[1].substring(6).replaceAll("\\+", " ");
                System.out.println(author + text);
                quoteService.add(author, text);
                sendToHomePage(out, true);
            }

            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendToHomePage(PrintWriter out, boolean redirect) throws IOException {
        String htmlResponse = "" +
                "<form method=\"POST\" action=\"save-quote\">" +
                "<label>Author: </label><br><br><input name=\"author\" ><br><br>" +
                "<label>Quote: </label><br><br><input name=\"quote\" ><br><br>" +
                "<button>Submit</button>" +
                "</form>"+
                "<label>Quote of the day: </label><br><br>";

        Socket helper = new Socket("localhost", 8084);
        BufferedReader inHelper = new BufferedReader(new InputStreamReader(helper.getInputStream()));
        PrintWriter outHelper = new PrintWriter(new OutputStreamWriter(helper.getOutputStream()), true);

        outHelper.println(gson.toJson(quoteService.getAllQuotes().size()));
        Integer response = gson.fromJson(inHelper.readLine(), Integer.class);
        System.out.println(response  + " je broj");
        htmlResponse += "<span style=\" font-weight: bold; border-style: solid \"  >"+quoteService.getQuoteAt(response)+" </span><br><br>";

        htmlResponse += "<div> ------------------------------------------------------------------------------------------------------ </div>";
        for (Quote quote : quoteService.getAllQuotes()){
            htmlResponse += "<label style=\" font-weight: bold; border-style: solid \"  >"+quote.toString()+" </label><br><br>";
        }
        String content = "<html><head><title>Odgovor servera</title></head>\n";
        content += "<body>" + htmlResponse + "</body></html>";
        if (redirect){
            out.println(new RedirectResponse("/quotes").getResponseString());
        }else{
            out.println(new HtmlResponse(content).getResponseString());
        }
    }

}