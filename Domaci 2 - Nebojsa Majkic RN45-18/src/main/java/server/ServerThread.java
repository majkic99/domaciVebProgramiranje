package server;

import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class ServerThread extends Thread {

	Socket socket;
	BufferedReader in;
	PrintWriter out;

	private Gson gson;
	private Table table;

	public ServerThread(Socket socket, Table table) {
		this.socket = socket;
		this.table = table;

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		gson = new Gson();
	}

	public void run() {
		Player player = null;
		try {
			Request request = receiveRequest();

			player = new Player(request.getId());

			Response response = new Response();
			response.setResult(Result.FAILURE);
			boolean active = true;
			if (request.getAction() == Action.REQUEST_CHAIR) {
				if (table.giveSeat(player)) {
					response.setResult(Result.SUCCESS);
					sendResponse(response);
				} else {
					sendResponse(response);
					active = false;
				}

			}
			int round = 1;
			
			while(active) {
				CountDownLatch latch = table.getCountdownMap().get(round);
				latch.countDown();
				try {
					//System.out.println("prvi" + latch.getCount());
					latch.await();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (table.gameEnded()) {
					response.setResult(Result.ENDED);
					sendResponse(response);
					active = false;
				}
				
				response.setResult(Result.ROUND_START);
				sendResponse(response);	
				
				request = receiveRequest();
				if (player != table.getCurrPlayer()) {
					//System.out.println("POSLAT GUESS  igracu " + player);
					response.setResult(Result.GUESS);
					sendResponse(response);
				}else {
					response.setResult(Result.DRAW);
					sendResponse(response);
				}
				request = receiveRequest();
				//System.out.println(request);
				switch (request.getAction()) {
				case ENDED:
					active = false;
					break;
				
				case DRAWN:
					
					try {
						latch = table.getGuessCountdownMap().get(round);
						latch.await();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					boolean out = table.draw(request.getChosen());
					if (out) {
						table.removePlayer(player);
						response.setResult(Result.ENDED);
						sendResponse(response);
						//System.out.println("Player " + player.getId() + " je izvukao kratak stapic i ispada"); 
						active = false;
					}else {
						//System.out.println("Player " + player.getId() + " je izvukao dugacak stapic i nastavlja sledeci igrac"); 
					}
					break;
				case GUESS_LONG:

					System.out.println(player + " je glasao za LONG");
					try {
						latch = table.getGuessCountdownMap().get(round);
						latch.countDown();
						//System.out.println("e"+round +" "+ latch.getCount() + " " + player);
						latch.await();
						
						latch = table.getDrawCountdownMap().get(round);
						latch.await();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					if (table.getCurrentRoundDrawResult() == Result.LONG_STICK) {
						table.getResult().put(player, table.getResult().get(player) + 1);
					}
					
					break;
				case GUESS_SHORT:
					try {
						System.out.println(player + " je glasao za SHORT");
						latch = table.getGuessCountdownMap().get(round);
						latch.countDown();
						//System.out.println("e"+round +" "+ latch.getCount() + " " + player);
						latch.await();
						
						latch = table.getDrawCountdownMap().get(round);
						latch.await();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						
						e1.printStackTrace();
					}
					
					if (table.getCurrentRoundDrawResult() == Result.SHORT_STICK) {
						table.getResult().put(player, table.getResult().get(player) + 1);
					}
					break;
				default:
					System.out.println(request.getAction());
					break;
				}
			
				latch = table.getEndRoundCountdownMap().get(round);
				latch.countDown();
				try {

					//System.out.println("zadnji" + latch.getCount());
					latch.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				round++;			
			}
			
			
			
			
			
			
			
		} catch (IOException e) {
			//e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			if (player != null) {
				if (table.getResult().containsKey(player)) {
					System.out.println(player + " ima : " + table.getResult().get(player) + " poena");
				}
				
			}
			
		}
	}

	public Request receiveRequest() throws IOException {
		return gson.fromJson(in.readLine(), Request.class);
	}

	private void sendResponse(Response response) {
		out.println(gson.toJson(response));
	}
}
