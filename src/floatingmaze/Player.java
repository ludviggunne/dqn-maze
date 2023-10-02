package floatingmaze;

import java.util.ArrayList;

/**
 * Player --- Represents the player in the game.
 */
public class Player {
	private double x, y, vx, vy, ax, ay;
	private int coordX, coordY, d, score; 
	
	public Player(int x, int y, int d) {
		this.setPosition(x, y); //set starting position
		this.d = d; //set player radius
	}
	
	// Accelerates the player in specified direction with specified force
	public void accelerate(Direction d, double force) {
		switch (d) {
			case UP:
				if (this.vy > (-1) * Constants.velMax) {
					this.ay = (-1) * force * Constants.forceScale;
				}
				else {
					this.ay = 0;
					this.vy = (-1) * Constants.velMax;
				}
				break;
			case DOWN:
				if (this.vy < Constants.velMax) {
					this.ay = force * Constants.forceScale;
				}
				else {
					this.ay = 0;
					this.vy = Constants.velMax;
				}
				break;
			case LEFT:
				if (this.vx > (-1) * Constants.velMax) {
					this.ax = (-1) * force * Constants.forceScale;
				}
				else {
					this.ax = 0;
					this.vx = (-1) * Constants.velMax;
				}
				break;
			case RIGHT:
				if (this.vx < Constants.velMax) {
					this.ax = force * Constants.forceScale;
				}
				else {
					this.ax = 0;
					this.vx = Constants.velMax;
				}
				break;
		}
	}
	
	// Stops acceleration in a specified direction
	public void stopAcceleration(Direction d) {
		if (d == Direction.UP || d == Direction.DOWN) {
			this.ay = 0;
		}
		else if (d == Direction.LEFT || d == Direction.RIGHT) {
			this.ax = 0;
		}
	}
	
	// Updates the position and velocity of the player, assuming there are no walls
	public void stepPosition() {
		this.x += vx;
		this.y += vy;
		this.coordX = (int) Math.floor(this.x); //rounded x-position
		this.coordY = (int) Math.floor(this.y); //rounded y-position
		this.vx += ax;
		this.vy += ay;
	}
	
	// Updates the position and velocity of the player, taking walls into account
	public void stepPosition(ArrayList<Wall> ws) {
		this.stepPosition();
		for (Wall w : ws) {
			switch (w.checkCollision(this)) {
				case RIGHT:
					this.x = w.x2;
					if (this.vx < 0) {
						this.vx = (-1) * this.vx;
					}
					break;
				case LEFT:
					this.x = w.x1 - this.d;
					if (this.vx > 0) {
						this.vx = (-1) * this.vx;
					}
					break;
				case ABOVE:
					this.y = w.y1 - this.d;
					if (this.vy > 0) {
						this.vy = (-1) * this.vy;
					}
					break;
				case BELOW:
					this.y = w.y2;
					if (this.vy < 0) {
						this.vy = (-1) * this.vy;
					}
					break;
				case NONE:
					break;
			}
		}
	}
	
	// Sets the position of the player
	public void setPosition(int x, int y) {
		this.coordX = x;
		this.coordY = y;
		this.x = (double) x;
		this.y = (double) y;
	}
	
	// Sets the velocity of the player
	public void setVelocity(int vx, int vy) {
		this.vx = vx;
		this.vy = vy;
	}
	
	// Sets the acceleration of the player
	public void setAcceleration(int ax, int ay) {
		this.ax = ax;
		this.ay = ay;
	}
	
	// Gets the x-coordinate of the player
	public int getXPosition() {
		return this.coordX;
	}
	
	// Gets the y-coordinate of the player
	public int getYPosition() {
		return this.coordY;
	}
	
	// Gets the x-velocity of the player
	public double getXVelocity() {
		return this.vx;
	}
	
	// Gets the y-velocity of the player
	public double getYVelocity() {
		return this.vy;
	}
	
	// Gets the x-acceleration of the player
	public double getXAcceleration() {
		return this.ax;
	}
	
	// Gets the y-acceleration of the player
	public double getYAcceleration() {
		return this.ay;
	}
	
	// Gets the size of the player
	public int getSize() {
		return this.d;
	}
	
	// Gets the score of the player
	public int getScore() {
		return this.score;
	}
	
	// Sets the score of the player
	public void setScore(int score) {
		this.score = score;
	}
	
	// Adds a score to the current score
	public void addScore(int score) {
		this.score += score;
	}
}