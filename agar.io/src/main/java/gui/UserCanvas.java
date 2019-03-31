package gui;

import java.awt.MouseInfo;
import java.awt.Point;

import client.Client;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UserCanvas extends AnchorPane {

	private Stage primaryStage;
	private Client user;
	
	public void start() {
		
		Canvas canvas = new Canvas(1024, 680);
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		this.getChildren().add(canvas);

		drawShapes(gc, user);
		
		// Clear away portions as the user drags the mouse
//		canvas.setOnMouseEntered(new EventHandler<MouseEvent>() {
//			public void handle(MouseEvent e) {
//				
//
//			}
//		});

	}

	private void drawShapes(final GraphicsContext gc, final Client user) {
		// infoPlayer = {x,y,w,h,id,T/F,R,G,B}  is the same for enemies and food
		final String[] infoPlayer = user.getInfoPlayer();
		int user_x = Integer.parseInt(infoPlayer[0]);
		int user_y = Integer.parseInt(infoPlayer[1]);
		double user_w = Double.parseDouble(infoPlayer[2]);
		double user_h = Double.parseDouble(infoPlayer[3]);
		final String user_id = infoPlayer[4];
		double R = Double.parseDouble(infoPlayer[6]);
		double G = Double.parseDouble(infoPlayer[7]);
		double B = Double.parseDouble(infoPlayer[8]);

		final Sprite player = new Sprite(user_id);
		player.setPosition(user_x, user_y);
		player.setMass(user_w, user_h);
		player.setLive(infoPlayer[5]);
		player.setColor(R, G, B, 1);
		player.render(gc);

		Timeline gameLoop = new Timeline();
		gameLoop.setCycleCount(Timeline.INDEFINITE);
		KeyFrame kf = new KeyFrame(Duration.seconds(0.04), // 0.017 -> 60 FPS
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent ae) {
						gc.clearRect(0, 0, 1024, 680);
						String[] players = user.getPlayersFromGame();
						for (String info : players) {
							String[] infoPlayer = info.split(",");
							if(infoPlayer[5].equals("T")) {
								int user_x = Integer.parseInt(infoPlayer[0]);
								int user_y = Integer.parseInt(infoPlayer[1]);
								double user_w = Double.parseDouble(infoPlayer[2]);
								double user_h = Double.parseDouble(infoPlayer[3]);
								String user_id = infoPlayer[4];
								double R = Double.parseDouble(infoPlayer[6]);
								double G = Double.parseDouble(infoPlayer[7]);
								double B = Double.parseDouble(infoPlayer[8]);
								final Sprite player = new Sprite(user_id);
								player.setPosition(user_x, user_y);
								player.setMass(user_w, user_h);
								player.setLive(infoPlayer[5]);
								player.setColor(R, G, B, 1);
								player.render(gc);
							}
							
						}

						String[] food = user.getFoodFromGame();
						for (String info : food) {
							String[] infoFood = info.split(",");
							if(infoFood[5].equals("T")) {
								int food_x = Integer.parseInt(infoFood[0]);
								int food_y = Integer.parseInt(infoFood[1]);
								double food_w = Double.parseDouble(infoFood[2]);
								double food_h = Double.parseDouble(infoFood[3]);
								
								double R = Double.parseDouble(infoFood[6]);
								double G = Double.parseDouble(infoFood[7]);
								double B = Double.parseDouble(infoFood[8]);

								final Sprite aFood = new Sprite(infoFood[4]);
								aFood.setPosition(food_x, food_y);
								aFood.setMass(food_w, food_h);
								aFood.setLive("T");
								aFood.setColor(R, G, B, 1);
								aFood.render(gc);
							}
							
						}

						Point xy = MouseInfo.getPointerInfo().getLocation();
						double dx = xy.getX() - player.x();
						double dy = xy.getY() - player.y();

						double theta = Math.atan2(dy, dx);

						int nx = (int) (player.velocityX * Math.cos(theta));
						int ny = (int) (player.velocityY * Math.sin(theta));
						player.changePosition(nx, ny);

						player.render(gc);
						String state = player.x()+","+ player.y()+ ","+ player.width() + ","+
								player.height() + "," +player.getId()+"T";
						user.updatePlayer(state);
					}
				});
		gameLoop.getKeyFrames().add(kf);
		gameLoop.play();

	}
	
	public void setClient(Client client) {
		user = client;
	}
	
	public Client getClient() {
		return user;
	}

}
