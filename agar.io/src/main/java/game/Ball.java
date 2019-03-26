package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball {

	public static final int MASA = 150;
    public static final int MAX_COLOR = 256;
    
    private double centerX, centerY;
    private int masa;
    private Color color;
    private static Random rand = new Random();
    private int timeCreation;
    private boolean principal;
	
    public Ball(int xMax, int yMax){
    	this.centerX = rand.nextInt(3*xMax/4)+ xMax/8;
        this.centerY = rand.nextInt(3*yMax/4)+ yMax/8;
        int r = rand.nextInt(MAX_COLOR);
        int g = rand.nextInt(MAX_COLOR);
        int b = rand.nextInt(MAX_COLOR);
        this.color = new Color(r,g,b);
        this.masa = MASA;
        this.timeCreation = 0;
    }

	public double getCenterX() {
		return centerX;
	}

	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}

	public int getMasa() {
		return masa;
	}

	public void setMasa(int masa) {
		this.masa = masa;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getTimeCreation() {
		return timeCreation;
	}

	public void setTimeCreation(int timeCreation) {
		this.timeCreation = timeCreation;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
    
	public void incrementarMasa(int value){
        this.masa += value;
    }
    
    public int generarRadio(){
        return (int)Math.sqrt(this.masa / Math.PI );
    }
    
    public void render(Graphics g, double scale){
        int r = this.generarRadio();
        g.setColor(this.color);
        g.fillOval((int) (this.centerX - r), (int) (this.centerY - r), 2*r, 2*r);
        g.setColor(Color.BLACK);
        g.drawOval((int) (this.centerX - r), (int) (this.centerY - r), 2*r, 2*r);    
    }
    
    public int verificarColision(Ball other){
        double d = distancia(this.centerX,this.centerY,other.centerX, other.centerY);
        if (d < this.generarRadio() + other.generarRadio()){
            if (this.masa > other.masa){
                return 1;
            } else if (this.masa < other.masa){
                return -1;
            } else
                return 0;
        } else{
            return 0;
        }
    }
    
    private double distancia(double xi, double yi, double xf, double yf){
        return Math.sqrt((yf-yi)*(yf-yi) + (xf-xi)*(xf-xi));
    }
}
