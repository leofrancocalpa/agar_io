package gui;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Sprite {

	public static final String PLAYER = "Player";
	public static final String FOOD = "Food";

	public static String type;
	private String id;
	private double positionX;
	private double positionY;
	public double velocityX;
	public double velocityY;
	private double width;
	private double height;
	public Color color;

	public Sprite(String id) {
		this.id = id;
		positionX = 0;
		positionY = 0;
		velocityX = 5;
		velocityY = 5;
	}

	public void setMass(double width, double height) {
		this.width += width;
		this.height += height;
	}

	public void setPosition(double x, double y) {
		positionX = x;
		positionY = y;
	}

	public double x() {
		return positionX;
	}

	public double y() {
		return positionY;
	}

	public double width() {
		return width;
	}

	public double height() {
		return height;
	}

	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
	}

	public void addVelocity(double x, double y) {
		velocityX += x;
		velocityY += y;
	}

	public void changePosition(int x, int y) {
		positionX += x;
		positionY += y;
	}

	public void update(double time) {
		positionX += velocityX * time;
		positionY += velocityY * time;
	}

	public void render(GraphicsContext gc) {
		gc.setFill(color);
		gc.fillOval(positionX, positionY, width, height);
	}

	public Rectangle2D getBoundary() {
		return new Rectangle2D(positionX, positionY, width, height);
	}

	public boolean intersects(Sprite s) {
		return s.getBoundary().intersects(this.getBoundary());
	}

	public String toString() {
		return " Position: [" + positionX + "," + positionY + "]" + " Velocity: [" + velocityX + "," + velocityY + "]";
	}

}
