package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;

public class Match 
{
	public static final int FOOD_NUM_MAX=100;
	public static final int FOOD_WIDTH=20;
	public static final int FOOD_HEIGHT=20;
	public static final int PLAYER_MIN_SIZE=30;
	
	ArrayList<Ball> food;
	ArrayList<Ball> players;
	//HashMap<String, Ball> players;
	private int game_field_size_x;
	private int game_field_size_y;
	
	public Match() {
		food = new ArrayList<Ball>();
		//players = new HashMap<String, Ball>();
		players = new ArrayList<Ball>();
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
			double R = rd.nextDouble();
			double G = rd.nextDouble();
			double B = rd.nextDouble();
			Color color = new Color(R,G,B,100);
			newFood.color=color;
			newFood.setPosition(posX, posY);
			food.set(i, newFood);
		}
		
		for(int i=0; i<playersFromServer.size(); i++) {
			Ball newPlayer = new Ball(playersFromServer.get(i));
			newPlayer.setVelocity(5, 5);
			newPlayer.setMass(PLAYER_MIN_SIZE, PLAYER_MIN_SIZE);
			int posX = (int) (Math.random()*game_field_size_x+1);
			int posY = (int) (Math.random()*game_field_size_y+1);
			Random rd = new Random();
			double R = rd.nextDouble();
			double G = rd.nextDouble();
			double B = rd.nextDouble();
			Color color = new Color(R,G,B,100);
			newPlayer.color=color;
			newPlayer.setPosition(posX, posY);
			//players.put(newPlayer.getId(), newPlayer);
			players.add(i, newPlayer);
		}
	}
	
	public void updateGame(String[] food, String[] players) {
		//update position and mass of players and food
		iterationInGame();
	}
	
	public void iterationInGame() {
		//Detect intersections and collitions between balls
	}
 
	/**
	 * 
	 * @return  Array of strings that represent the position x,y and mass (width and height) of every food in the game
	 * Example : food[0] -> x,y,w,h
	 */
	public String[] getFoodFromGame() {
		String[] food = new String[FOOD_NUM_MAX];
		for(int i=0; i<FOOD_NUM_MAX; i++) {
			food[i]=this.food.get(i).toString();
		}
		return food;
	}
	/**
	 * 
	 * @return Array of strings that represent the position x,y and mass (width and height) of every player in the game
	 * Example : player[0] -> x,y,w,h,id
	 */
	public String[] getPlayerFromGame() {
		String[] players = new String[this.players.size()];
		for(int i=0; i<players.length; i++) {
			players[i]=this.players.get(i).toString();
		}
		
		return players;
	}
		
}
