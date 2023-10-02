package floatingmaze;

/**
 * Wall --- Represents a wall which bounces the player upon impact.
 */
public class Wall {
	public int x1, x2, y1, y2;
	
	public Wall(int x1, int x2, int y1, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	// Checks whether, and from which direction, the player is in contact with the Wall
	public Collision checkCollision(Player p) {
		if (p.getXPosition() + p.getSize() > x1 &&
		    p.getXPosition() < x2 &&
		    p.getYPosition() + p.getSize() > y1 &&
		    p.getYPosition() < y2
		    ) {
			//calculate the midpoint coordinate of the player
			int midX = (int) (p.getXPosition() + Math.floor(p.getSize() / 2));
			int midY = (int) (p.getYPosition() + Math.floor(p.getSize() / 2));
			
			//calculate from which direction the collision takes place
			if (midX < (x1 + x2) / 2) { //left
				if (midY < (y1 + y2) / 2) { //above
					if (midX - x1 < midY - y1) {
						return Collision.LEFT;
					}
					else {
						return Collision.ABOVE;
					}
				}
				else { //below
					if (midX - x1 < y2 - midY) {
						return Collision.LEFT;
					}
					else {
						return Collision.BELOW;
					}
				}
			}
			else { //right
				if (midY < (y1 + y2) / 2) { //above
					if (x2 - midX < midY - y1) {
						return Collision.RIGHT;
					}
					else {
						return Collision.ABOVE;
					}
				}
				else { //below
					if (x2 - midX < y2 - midY) {
						return Collision.RIGHT;
					}
					else {
						return Collision.BELOW;
					}
				}
			}
		}
		else {
			return Collision.NONE;
		}
	}
}