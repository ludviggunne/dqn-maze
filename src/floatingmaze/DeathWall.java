package floatingmaze;

/**
 * DeathWall --- Represents a wall that immediately kills the player and restarts the game upon impact
 */
public class DeathWall {
	public int x1, x2, y1, y2;
	
	public DeathWall(int x1, int x2, int y1, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	// Checks whether the player is in contact with the DeathWall
	public boolean checkCollision(Player p) {
		return p.getXPosition() + p.getSize() > this.x1 &&
			   p.getXPosition() < this.x2 &&
			   p.getYPosition() + p.getSize() > this.y1 &&
			   p.getYPosition() < this.y2;
	}
}