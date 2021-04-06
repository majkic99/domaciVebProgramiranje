package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private static Map<String, Map<String, Integer>> database;

    private Database(){

    }
    public static Map<String, Map<String, Integer>> getDatabaseInstance(){
        if (database == null){
            database = new ConcurrentHashMap<>();
            database.put("Monday", new ConcurrentHashMap<String, Integer>());
            database.put("Tuesday", new ConcurrentHashMap<String, Integer>());
            database.put("Wednesday", new ConcurrentHashMap<String, Integer>());
            database.put("Thursday", new ConcurrentHashMap<String, Integer>());
            database.put("Friday", new ConcurrentHashMap<String, Integer>());
            try {
                fillDB("E:\\GitRepos\\domaciVebProgramiranje\\Domaci 4 - Nebojsa Majkic RN45-18\\src\\main\\resources\\ponedeljak.txt","Monday");
                fillDB("E:\\GitRepos\\domaciVebProgramiranje\\Domaci 4 - Nebojsa Majkic RN45-18\\src\\main\\resources\\utorak.txt","Tuesday");
                fillDB("E:\\GitRepos\\domaciVebProgramiranje\\Domaci 4 - Nebojsa Majkic RN45-18\\src\\main\\resources\\sreda.txt","Wednesday");
                fillDB("E:\\GitRepos\\domaciVebProgramiranje\\Domaci 4 - Nebojsa Majkic RN45-18\\src\\main\\resources\\cetvrtak.txt","Thursday");
                fillDB("E:\\GitRepos\\domaciVebProgramiranje\\Domaci 4 - Nebojsa Majkic RN45-18\\src\\main\\resources\\petak.txt","Friday");

            }catch(Exception e){
                e.printStackTrace();
            }

            return database;
        }else{
            return database;
        }
    }
    
    private static void fillDB(String location, String dbKey) throws FileNotFoundException {
    	Scanner scanner = new Scanner(new File(location));
    	while (scanner.hasNextLine()){
            database.get(dbKey).put(scanner.nextLine(), 0);
            System.out.println(database.get(dbKey).size());
        }
    }
    

}
