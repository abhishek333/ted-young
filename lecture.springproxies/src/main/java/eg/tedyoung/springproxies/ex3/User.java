package eg.tedyoung.springproxies.ex3;


public class User {
	boolean authenticated;
	
	public User() {
	}

	boolean isAuthenticated() {
		return authenticated;
	}

	void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
	
	
}
