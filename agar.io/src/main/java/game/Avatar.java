package game;


public class Avatar extends Ball{

	private static final double INIT_VEL = 3.5;
    private static final int TIME_CREATION_MAX = 10000; 
    
    private int id;
    private String nickname;
    private double vel;
    private double vectorX, vectorY;
    private boolean mustDie;
	
	public Avatar(int id, String nickname, int xMax, int yMax) {
		super(xMax, yMax);
		
		this.id = id;
        this.nickname = nickname;
        this.vectorX = this.vectorY = this.vel = 0;
        this.mustDie = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public double getVel() {
		return vel;
	}

	public void setVel(double vel) {
		this.vel = vel;
	}

	public double getVectorX() {
		return vectorX;
	}

	public void setVectorX(double vectorX) {
		this.vectorX = vectorX;
	}

	public double getVectorY() {
		return vectorY;
	}

	public void setVectorY(double vectorY) {
		this.vectorY = vectorY;
	}

	public boolean isMustDie() {
		return mustDie;
	}

	public void setMustDie(boolean mustDie) {
		this.mustDie = mustDie;
	}

	

}
