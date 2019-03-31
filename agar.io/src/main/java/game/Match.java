package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


import javafx.scene.paint.Color;

public class Match 
{
	public static final int FOOD_NUM_MAX=100;
	public static final int FOOD_WIDTH=20;
	public static final int FOOD_HEIGHT=20;
	public static final int PLAYER_MIN_SIZE=30;
	
	//ArrayList<Ball> food;
	//ArrayList<Ball> players;
	HashMap<String, Ball> food;
	HashMap<String, Ball> players;
	private int game_field_size_x;
	private int game_field_size_y;
	
	public Match() {
		//food = new ArrayList<Ball>();
		//players = new ArrayList<Ball>();
		food = new HashMap<String, Ball>();
		players = new HashMap<String, Ball>();
		this.game_field_size_x=1024;
		this.game_field_size_y=680;
	}
	
	public void initialize(ArrayList<String> playersFromServer) {
		for(int i=0; i<FOOD_NUM_MAX; i++) {
			Ball newFood = new Ball(i+"");
			newFood.setVelocity(0, 0);
			newFood.setMass(FOOD_WIDTH, FOOD_HEIGHT);
			int posX = (int) (Math.random()*game_field_size_x+1);
			int posY = (int) (Math.random()*game_field_size_y+1);
			Random rd = new Random();
			newFood.colors[0] = rd.nextDouble();
			newFood.colors[1] = rd.nextDouble();
			newFood.colors[2] = rd.nextDouble();
			newFood.setPosition(posX, posY);
			//food.set(i, newFood);
			food.put(newFood.getId(), newFood);
		}
		
		for(int i=0; i<playersFromServer.size(); i++) {
			Ball newPlayer = new Ball(playersFromServer.get(i));
			newPlayer.setVelocity(5, 5);
			newPlayer.setMass(PLAYER_MIN_SIZE, PLAYER_MIN_SIZE);
			int posX = (int) (Math.random()*game_field_size_x+1);
			int posY = (int) (Math.random()*game_field_size_y+1);
			Random rd = new Random();
			newPlayer.colors[0] = rd.nextDouble();
			newPlayer.colors[1] = rd.nextDouble();
			newPlayer.colors[2] = rd.nextDouble();
			newPlayer.setPosition(posX, posY);
			players.put(newPlayer.getId(), newPlayer);
			//players.add(i, newPlayer);
		}
	}
	
	/**
	 * 
	 * @param food array with info about all ball of food on game. {x,y,w,h,id,T/F}
	 * @param players {x,y,w,h,id,T/F}
	 */
	public void updateGame(String[] players) {
		//update position and mass of players and food
//		for(String data : food) {
//			String[] info = data.split(",");
//			int x = Integer.parseInt(info[0]);
//			int y = Integer.parseInt(info[1]);
//			Ball ball = this.food.get(info[4]);
//			ball.setPosition(x, y);
//			if(info[5].equals("T")) ball.setAlive(true);
//			if(info[5].equals("F")) ball.setAlive(false);
//		}
//		
		for(String data : players) {
			String[] info = data.split(",");
			int x = (int) Double.parseDouble(info[0]);
			int y = (int) Double.parseDouble(info[1]);
			Ball ball = this.players.get(info[4]);
			ball.setPosition(x, y);
			if(info[5].equals("T")) ball.setAlive(true);
			if(info[5].equals("F")) ball.setAlive(false);
		}
		
		iterationInGame();
	}
	
	public void iterationInGame() {
		//Detect intersections and collitions between balls
		@SuppressWarnings("unchecked")
		HashMap<String, Ball> hm = (HashMap<String, Ball>) players.clone();
		compareIntersections(hm);
	}
	
	public void compareIntersections(HashMap<String, Ball> hm) {
		if(hm.size()<2) {
			//do nothing
		}
		else {
			Iterator<Map.Entry<String, Ball>> iterator = hm.entrySet().iterator();
			Map.Entry<String, Ball> map = iterator.next();
			while(iterator.hasNext()) {
				if(players.get(map.getKey()).getIsAlive()==false) {
					break;
				}
				Map.Entry<String, Ball> compare = iterator.next();
				if(players.get(map.getKey()).intersects(players.get(compare.getKey()))) {
					fight(players.get(map.getKey()), players.get(compare.getKey()));
				}
			}
			hm.remove(map.getKey(), map.getValue());
			compareIntersections(hm);
		}
		
	}
	
	public void fight(Ball b1, Ball b2) {
		if(b1.width()>b2.width()) {
			b1.setMass(b2.width(), b2.width());
			b2.setAlive(false);
		}
		if(b1.width()<b2.width()) {
			b2.setMass(b1.width(), b1.width());
			b1.setAlive(false);
		}
	}
 
	/**
	 * 
	 * @return  Array of strings that represent the position x,y and mass (width and height) of every food in the game
	 * Example : food[0] -> {x,y,w,h,id,T/F,R,G,B}
	 */
	public String[] getFoodFromGame() {
		String[] balls = new String[FOOD_NUM_MAX];
		int i=0;
		for(Map.Entry<String, Ball> element : food.entrySet()) {
			balls[i]=element.getValue().toString();
			i++;
		}
		return balls;
	}
	/**
	 * 
	 * @return Array of strings that represent the position x,y and mass (width and height) of every player in the game
	 * Example : player[0] -> {x,y,w,h,id,T/F,R,G,B}
	 */
	public String[] getPlayersFromGame() {
		String[] balls = new String[this.players.size()];
		int i=0;
		for(Map.Entry<String, Ball> element : players.entrySet()) {
			balls[i]=element.getValue().toString();
			i++;
		}
		
		return balls;
	}
		
}
