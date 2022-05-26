import java.time.LocalDate;

public class LoginReward {

	private static final int DAILY_REWARD = 100;
	private int currentPoints;
	private LocalDate lastLoginDate;
	private LocalDate thisLoginDate;

	// Get lastLoginDate and currentPoints from database
	public LoginReward(LocalDate date, int currentPoints) {
		this.currentPoints = currentPoints;
		this.lastLoginDate = date;
		this.thisLoginDate = LocalDate.now();
	}

	// If the user hasn't logged in today (lastLoginDate != thisLoginDate), the user
	// gets 100 points,
	// update lastLoginDate to thisLoginDate, and return currentPoints after getting
	// the daily reward.
	// If not, only return currentPoints.
	public boolean getReward() {
		if (!this.thisLoginDate.equals(this.lastLoginDate)) {
			this.lastLoginDate = this.thisLoginDate;
			return true;
		}
		else {
			return false;
		}
		

	}

	// Getter for last login date
	public LocalDate getLastLoginDate() {
		return this.lastLoginDate;
	}

}
