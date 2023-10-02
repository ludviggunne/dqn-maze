package floatingmaze;

/**
 * Goal --- Represents a goal that awards the player with points and restarts the game upon impact.
 */
public class Goal {
	
	public int x1, x2, y1, y2;
	private int score;
	
	public Goal(int x1, int x2, int y1, int y2, int score) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.score = score;
	}
	
	// Checks whether the player has reached the goal
	public boolean contains(Player p) {
		return p.getXPosition() + p.getSize() > this.x1 &&
			   p.getXPosition() < this.x2 &&
			   p.getYPosition() + p.getSize() > this.y1 &&
			   p.getYPosition() < this.y2;
	}
	
	// Gets the score associated with the Goal
	public int getScore() {
		return score;
	}
}