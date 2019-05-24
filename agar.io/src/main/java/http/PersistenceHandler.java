package http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Dictionary;
import java.util.HashMap;

public class PersistenceHandler {
	
	public static final String USERS="src/main/resources/server/persistance/users.txt";
	public static final String MATCHS_DATES="src/main/resources/server/persistance/match_dates.txt";
	
	private HashMap<String, String> users;
	private HashMap<String, String> usersNoMail;
	private HashMap<String, String> matchs;
	private static PersistenceHandler auth;
	
	private PersistenceHandler() {
		users= new HashMap<String, String>();
		usersNoMail= new HashMap<String, String>();
		matchs = new HashMap<String, String>();
		loadUsersDB();
		loadMatchsDB();
	}
	
	public static  PersistenceHandler getPersistanceHandler() {
		if(auth==null) {
			auth = new PersistenceHandler();
		}
		return auth;
	}
	
	private void loadUsersDB() {
		String route = USERS;
		try {
			BufferedReader br = new BufferedReader(new FileReader(route));
			String line = br.readLine();
			line = br.readLine();
			while(line != null && !line.equals("")) {
				System.out.println("user loaded: "+line);
				String[] data = line.split("-");
				String[] dataNoMail = data[1].split("/");
				System.out.println("este : "+dataNoMail[1]);
				users.put(data[0], data[1]);
				usersNoMail.put(data[0], dataNoMail[0]);
				line=br.readLine();
			}
			br.close();
			
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void loadMatchsDB() {
		String route = MATCHS_DATES;
		try {
			BufferedReader br = new BufferedReader(new FileReader(route));
			String fecha = br.readLine();
			String line = br.readLine();
			String data = "";
			while(line != null && !line.equals("")) {
				
				if(!line.equals("---")) {
					if(data.equals("")) {
						data=line;
					}
					else {
						data+="-"+line;
					}
					
				}
				else {
					System.out.println("match loaded: "+fecha+" "+data);
					matchs.put(fecha, data);
					data="";
					fecha=br.readLine();
				}
				
				line=br.readLine();
			}
			br.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean addUser(String username, String password, String mail) {
		if(!users.containsKey(username)) {
			users.put(username, password+"/"+mail);
			usersNoMail.put(username, password);
			persisUser(username, password,mail);
			return true;
		}
		else {
			return false;
		}
	}
	
	public void persisUser(String username, String password, String mail) {
		
		try {
			File fout = new File(USERS);
			FileOutputStream fos = new FileOutputStream(fout, true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.newLine();
			bw.write(username+"-"+password+"/"+mail);
			bw.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean addMatchDates(String date, String[] data) {
		if(!matchs.containsKey(date)) {
			String users =data[0];
			for(int i=1; i<data.length; i++) {
				users+="-"+data[i];
			}
			matchs.put(date, users);
			persisMatch(date, data);
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public void persisMatch(String date, String[] data){
		try {
			File fout = new File(MATCHS_DATES);
			FileOutputStream fos = new FileOutputStream(fout, true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.newLine();
			bw.write(date);
			for(int i=0; i<data.length; i++) {
				bw.newLine();
				bw.write(data[i]);
			}
			bw.newLine();
			bw.write("---");
			bw.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean conatainsUser(String username, String password) {
		boolean salida = false;
		if(usersNoMail.containsKey(username)) {
			if(usersNoMail.get(username).equals(password)) {
				salida = true;
			}
		}
		return salida;
	}
	
	public HashMap<String, String> getUsers(){
		return users;
	}
	
	public HashMap<String, String> getMatchs(){
		HashMap<String, String> clone = matchs;
		return clone;
	}

}
