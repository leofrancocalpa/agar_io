package gui;

import java.awt.MouseInfo;
import java.awt.Point;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
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

public class UserCanvas extends Application{


 
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Agar.io");
        Group root = new Group();
        Canvas canvas = new Canvas(1024,680);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        
        final Sprite player = new Sprite();
        player.setPosition(232, 128);
    	player.setMass(35,35);
    	player.render(gc);
    	
        drawShapes(gc, player);
        
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setFullScreenExitHint("Juego Iniciado");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        // Clear away portions as the user drags the mouse
        canvas.setOnMouseMoved( 
        new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                
                
                
            }
        });

    }
    
    

    private void drawShapes(final GraphicsContext gc, final Sprite player) {
    	
    	
    	
    	final LongValue lastNanoTime = new LongValue( System.nanoTime() );

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 100000000000.0;
                lastNanoTime.value = currentNanoTime;
                
                // game logic
                Point xy = MouseInfo.getPointerInfo().getLocation();
                double dx = xy.getX() - player.x();
                double dy = xy.getY() - player.y();
                
                double theta = Math.atan2(dy, dx);
                
                int nx = (int)(player.velocityX * Math.cos(theta));
                int ny = (int)(player.velocityY * Math.sin(theta));
                player.changePosition(nx, ny);
                
                gc.clearRect(0, 0, 1024, 680);
                player.render(gc);
            }
        }.start();
       
    }

	  public static void main(String[] args) {
	    launch(args);
	  }

}

