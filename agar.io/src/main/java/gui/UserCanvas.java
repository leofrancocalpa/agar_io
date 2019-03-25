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
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        // Clear away portions as the user drags the mouse
        canvas.setOnMouseMoved( 
        new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                //gc.clearRect(e.getX() - 2, e.getY() - 2, 5, 5);
            }
        });
  
     // Fill the Canvas with a Blue rectnagle when the user double-clicks
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, 
         new EventHandler<MouseEvent>() {
             public void handle(MouseEvent t) {            
                  
             }
         });
        
        
    }
    
    

    private void drawShapes(final GraphicsContext gc) {
    	final Sprite player = new Sprite();
    	player.setPosition(232, 128);
    	player.setMass(35,35);
    	player.render(gc);
    	
    	final LongValue lastNanoTime = new LongValue( System.nanoTime() );

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 100000000000.0;
                lastNanoTime.value = currentNanoTime;
                
                // game logic
                Point mousePosition = MouseInfo.getPointerInfo().getLocation();
                
                int distanceSpriteMouseX = mousePosition.x - (int)player.x();
                int distanceSpriteMouseY = mousePosition.y - (int)player.y();
                
                if(distanceSpriteMouseY<0) {
                	player.addVelocity(0, -5);
                }
                if(distanceSpriteMouseY>0) {
                	player.addVelocity(0, 5);
                }
                if(distanceSpriteMouseX<0) {
                	player.addVelocity(-5, 0);
                }
                if(distanceSpriteMouseX>0) {
                	player.addVelocity(5, 0);
                }
                player.update(elapsedTime);
                
                //Render 
                gc.clearRect(0, 0, 400, 350);
                player.render(gc);
            }
        }.start();
       
    }

	  public static void main(String[] args) {
	    launch(args);
	  }

}

