package server;

public class User {

	private String email;
	private String nickname;
	private String password;
	private boolean inGame;
	
	public User(String pEmail, String pNickname, String pPassword) {
		email = pEmail;
		nickname = pNickname;
		password = pPassword;
		setInGame(false);
	}
	
	public String getEmail() {
		return email;
	}
	public String getNickname() {
		return nickname;
	}
	public String getPassword() {
		return password;
	}
	public boolean isInGame() {
		return inGame;
	}
	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
}
