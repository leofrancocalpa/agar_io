package game;

import java.util.ArrayList;

public class Match 
{
 
	private ArrayList<Ball> balls;
	
	public Match(int xMax, int yMax) {
		Ball ball = new Ball(xMax, yMax);
        ball.setPrincipal(true);
        this.balls = new ArrayList<Ball>();
        this.balls.add(ball);
	}
	
}
