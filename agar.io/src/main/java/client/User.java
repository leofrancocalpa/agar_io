package client;

public class User {

	private String email;
	private String nickname;
	private String password;
	
	public User(String pEmail, String pNickname, String pPassword) {
		email = pEmail;
		nickname = pNickname;
		password = pPassword;
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
}
