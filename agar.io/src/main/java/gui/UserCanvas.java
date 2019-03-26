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
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UserCanvas extends Application {

	private Stage primaryStage;
	private Client user;

	@Override
	public void start(Stage PprimaryStage) {
		primaryStage = PprimaryStage;
		user = new Client();

		primaryStage.setTitle("Agar.io");
		Group root = new Group();
		Canvas canvas = new Canvas(1024, 680);
		final GraphicsContext gc = canvas.getGraphicsContext2D();

//      final Sprite player = new Sprite("ww");
//      player.setPosition(232, 128);
//    	player.setMass(35,35);
//    	player.render(gc);

		drawShapes(gc, user);

		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		primaryStage.setFullScreenExitHint("Juego Iniciado");
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
		primaryStage.show();
		// Clear away portions as the user drags the mouse
		canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {

			}
		});

	}

	private void drawShapes(final GraphicsContext gc, final Client user) {

		String[] infoPlayer = user.getInfoPlayer();
		int user_x = Integer.parseInt(infoPlayer[0]);
		int user_y = Integer.parseInt(infoPlayer[1]);
		int user_w = Integer.parseInt(infoPlayer[2]);
		int user_h = Integer.parseInt(infoPlayer[3]);
		String user_id = infoPlayer[4];

		final Sprite player = new Sprite(user_id);
		player.setPosition(user_x, user_y);
		player.setMass(user_w, user_h);
		player.render(gc);

		Timeline gameLoop = new Timeline();
		gameLoop.setCycleCount(Timeline.INDEFINITE);
		KeyFrame kf = new KeyFrame(Duration.seconds(0.04), // 0.017 -> 60 FPS
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent ae) {
						String[] players = user.getPlayerFromGame();
						for (String info : players) {
							String[] infoPlayer = info.split(",");
							int user_x = Integer.parseInt(infoPlayer[0]);
							int user_y = Integer.parseInt(infoPlayer[1]);
							int user_w = Integer.parseInt(infoPlayer[2]);
							int user_h = Integer.parseInt(infoPlayer[3]);
							String user_id = infoPlayer[4];

							final Sprite player = new Sprite(user_id);
							player.setPosition(user_x, user_y);
							player.setMass(user_w, user_h);
							player.render(gc);
						}

						String[] food = user.getFoodFromGame();
						for (String info : food) {
							String[] infoFood = info.split(",");
							int food_x = Integer.parseInt(infoFood[0]);
							int food_y = Integer.parseInt(infoFood[1]);
							int food_w = Integer.parseInt(infoFood[2]);
							int food_h = Integer.parseInt(infoFood[3]);

							final Sprite aFood = new Sprite(Sprite.FOOD);
							aFood.setPosition(food_x, food_y);
							aFood.setMass(food_w, food_h);
							aFood.render(gc);
						}

						Point xy = MouseInfo.getPointerInfo().getLocation();
						double dx = xy.getX() - player.x();
						double dy = xy.getY() - player.y();

						double theta = Math.atan2(dy, dx);

						int nx = (int) (player.velocityX * Math.cos(theta));
						int ny = (int) (player.velocityY * Math.sin(theta));
						player.changePosition(nx, ny);

						gc.clearRect(0, 0, 1024, 680);
						player.render(gc);
						String[] state = { player.x() + "", player.y() + "", player.width() + "",
								player.height() + "" };
						user.updatePlayer(state);
					}
				});
		gameLoop.getKeyFrames().add(kf);
		gameLoop.play();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
