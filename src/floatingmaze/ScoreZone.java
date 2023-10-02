package floatingmaze;

/**
 * ScoreZone --- Represents a zone that disappears and awards point upon impact.
 */
public class ScoreZone {
	public int x1, x2, y1, y2;
	private int score;
	private boolean used; //indicates whether the score has been collected
	
	public ScoreZone(int x1, int x2, int y1, int y2, int score) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.score = score;
		this.used = false;
	}
	
	// Checks whether the player is in contact with the ScoreZone
	public boolean contains(Player p) {
		return p.getXPosition() + p.getSize() > this.x1 &&
			   p.getXPosition() < this.x2 &&
			   p.getYPosition() + p.getSize() > this.y1 &&
			   p.getYPosition() < this.y2;
	}
	
	// Gets the score associated with the ScoreZone
	public int getScore() {
		return score;
	}
	
	// Sets the used-status
	public void setStatus(boolean used) {
		this.used = used;
	}
	
	// Gets the used-status
	public boolean isUsed() {
		return this.used;
	}
}