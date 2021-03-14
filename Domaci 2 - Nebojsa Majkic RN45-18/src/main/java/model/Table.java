package model;

import java.security.KeyStore.Entry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class Table {
	
	private int numberOfRounds;
    private Player[] players;
    private Map<Player, Integer> result;
    private int currRound;
    private int currPlayer;
    private Map<Integer, Result> drawResultMap;
    private int numberOfPlayersAtTable;
    private Map<Integer, CountDownLatch> countdownMap;
    private Map<Integer, CountDownLatch> guessCountdownMap;
    private Map<Integer, CountDownLatch> drawCountdownMap;
    private Map<Integer, CountDownLatch> endRoundCountdownMap;
    
    public Table(int numberOfRounds) {
        this.players = new Player[6];
        result = new ConcurrentHashMap<>();
        currRound = 1;
        currPlayer = 0;
        this.numberOfRounds = numberOfRounds;
        drawResultMap = new ConcurrentHashMap<>();
        numberOfPlayersAtTable = 0;
        countdownMap = new ConcurrentHashMap<>();
        countdownMap.put(1, new CountDownLatch(6));
        guessCountdownMap = new ConcurrentHashMap<>();
        guessCountdownMap.put(1, new CountDownLatch(6-1));
        drawCountdownMap = new ConcurrentHashMap<>();
        drawCountdownMap.put(1, new CountDownLatch(1));
        endRoundCountdownMap = new ConcurrentHashMap<>();
        endRoundCountdownMap.put(1, new CountDownLatch(6));
        
    }
    
    public synchronized boolean firstStart() {
    	for (int i = 0; i < 6 ; i++) {
            if(players[i] == null) {
                return false;
                
            }
        }
    	return true;
    }
    
    public boolean draw(int chosen) {
    	Random r = new Random();
    	int draw = r.nextInt(5) + 1;
    	System.out.println("KRATKI STAPIC JE ONAJ SA BROJEM " + draw);
    	System.out.println(getCurrPlayer() + "JE IZABRAO STAPIC SA BROJEM " + chosen); 
    	System.out.println("CURRENT PLAYER - " + currPlayer + "---------------------------------------------------------") ;
    	boolean out = false;
    	if (chosen == draw) {
    		drawResultMap.put(currRound, Result.SHORT_STICK);
    		currPlayer = 0;
    		out = true;
    	}else {
    		drawResultMap.put(currRound, Result.LONG_STICK);
    		currPlayer++;
    		currPlayer = currPlayer % numberOfPlayersAtTable;
    	}
    	
    	
    	//guessMap = new ConcurrentHashMap<Player, Boolean>();
    	drawCountdownMap.get(currRound).countDown();
    	currRound++;
    	drawCountdownMap.put(currRound, new CountDownLatch(1));
    	countdownMap.put(currRound, new CountDownLatch(numberOfPlayersAtTable));
    	endRoundCountdownMap.put(currRound, new CountDownLatch(numberOfPlayersAtTable));
    	guessCountdownMap.put(currRound, new CountDownLatch(numberOfPlayersAtTable-1));
    	return out;
    	
    }
    
    
    
    public synchronized boolean giveSeat(Player player) {
        for (int i = 0; i < 6 ; i++) {
            if(players[i] == null) {
            	System.out.println(player.getId().toString() + " na mestu " + i);
                players[i] = player;
                result.put(player, 0);
                numberOfPlayersAtTable++;
                return true;
            }
        }
        return false;
    }
    
    public boolean gameEnded() {
    	if (currRound > numberOfRounds) {
    		return true;
    	}
    	return false;
    }

	public  Player[] getPlayers() {
		return players;
	}

	public Player getCurrPlayer() {
		return players[currPlayer];
	}
	
	public synchronized void removePlayer(Player player) {
		int i = 0;
		for (i = 0; i < 6; i++) {
			if (players[i] == player) {
				players[i] = null;
				numberOfPlayersAtTable--;
				rearrangePlayers(i);
				break;
			}
		}
		System.out.println(player + " je izbacen ===================================================");
		countdownMap.put(currRound, new CountDownLatch(numberOfPlayersAtTable));
		endRoundCountdownMap.put(currRound, new CountDownLatch(numberOfPlayersAtTable));
		guessCountdownMap.put(currRound, new CountDownLatch(numberOfPlayersAtTable-1));
		//System.out.println(Arrays.asList(players));
		
	}
	
	private void rearrangePlayers(int from) {
		if (from == 5) {
			return;
		}
		//System.out.println(from + "sada");
		int i = 0;
		for (i = from; i < numberOfPlayersAtTable; i++) {
				players[i] = players[i+1];
		}
		players[i] = null;
		/*
		synchronized(System.out) {
			System.out.println("-------");
			System.out.println(players[0]!=null ? players[0] : "NISTA");
			System.out.println(players[1]!=null ? players[1] : "NISTA");
			System.out.println(players[2]!=null ? players[2] : "NISTA");
			System.out.println(players[3]!=null ? players[3] : "NISTA");
			System.out.println(players[4]!=null ? players[4] : "NISTA");
			System.out.println(players[5]!=null ? players[5] : "NISTA");
			System.out.println("-------");
		}
		*/
		
	}

	
	public Result getCurrentRoundDrawResult() {
		return drawResultMap.get(currRound-1);
	}

	public Map<Player, Integer> getResult() {
		return result;
	}

	public int getNumberOfRounds() {
		return numberOfRounds;
	}

	public void setNumberOfRounds(int numberOfRounds) {
		this.numberOfRounds = numberOfRounds;
	}

	public int getCurrRound() {
		return currRound;
	}

	public void setCurrRound(int currRound) {
		this.currRound = currRound;
	}

	public Map<Integer, Result> getDrawResultMap() {
		return drawResultMap;
	}

	public void setDrawResultMap(Map<Integer, Result> drawResultMap) {
		this.drawResultMap = drawResultMap;
	}

	
	public int getNumberOfPlayersAtTable() {
		return numberOfPlayersAtTable;
	}

	public void setNumberOfPlayersAtTable(int numberOfPlayersAtTable) {
		this.numberOfPlayersAtTable = numberOfPlayersAtTable;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public void setResult(Map<Player, Integer> result) {
		this.result = result;
	}

	public void setCurrPlayer(int currPlayer) {
		this.currPlayer = currPlayer;
	}

	public Map<Integer, CountDownLatch> getCountdownMap() {
		return countdownMap;
	}

	public Map<Integer, CountDownLatch> getGuessCountdownMap() {
		return guessCountdownMap;
	}

	public void setGuessCountdownMap(Map<Integer, CountDownLatch> guessCountdownMap) {
		this.guessCountdownMap = guessCountdownMap;
	}

	public Map<Integer, CountDownLatch> getDrawCountdownMap() {
		return drawCountdownMap;
	}

	public Map<Integer, CountDownLatch> getEndRoundCountdownMap() {
		return endRoundCountdownMap;
	}
	



}
