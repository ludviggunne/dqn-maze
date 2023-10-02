package floatingmaze;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * World --- The JPanel-class representing the game environment.
 */
public class World extends JPanel implements ActionListener {
	private Player p;
	public int keyPressed;
	private Timer timer;
	private boolean trainingMode;
	private ArrayList<DeathWall> dws;
	private ArrayList<Wall> ws;
	private ArrayList<Goal> gs;
	private ArrayList<ScoreZone> szs;
	
	// Instantiates the world
	public World(Player p, boolean trainingMode) {
		this.p = p;
		this.trainingMode = trainingMode;
		this.ws = new ArrayList<Wall>();
		this.dws = new ArrayList<DeathWall>();
		this.gs = new ArrayList<Goal>();
		this.szs = new ArrayList<ScoreZone>();
		
		//if not launched in training mode, listen for key presses
		if (!this.trainingMode) {
			this.addKeyListener(new TAdapter());
			this.timer = new Timer(Constants.timeStep, this);
			timer.start();
		}
	}
	
	// When not in training mode, updates the world every time step
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean finished = update();
		if (finished) {
			this.reset();
		}
	}
	
	// Updates the world one training step with inputs and returns information about the game state
	public int[] trainingStepRL(Direction input, boolean acc) {
		int[] RLInfo = new int[2];
		this.stepPlayer(input, acc);
		boolean finished = this.update(); //whether the game is finished or not
		int score = this.p.getScore(); //the current player score
		if (finished) {
			this.reset();
		}
		RLInfo[0] = score;
		RLInfo[1] = finished ? 1 : 0; //translate to number that can be sent to Python
		return RLInfo;
	}
	
	// Updates the world one training step without any inputs and returns information about the game state
	public int[] trainingStepRL() {
		int[] RLInfo = new int[2];
		boolean finished = this.update(); //whether the game is finished or not
		int score = this.p.getScore(); //the current player score
		if (finished) {
			this.reset();
		}
		RLInfo[0] = score;
		RLInfo[1] = finished ? 1 : 0; //translate to number that can be sent to Python
		return RLInfo;
	}
	
	// Updates the player position one step, based on input
	private void stepPlayer(Direction input, boolean acc) {
		if (acc) {
			this.p.accelerate(input, Constants.accelerationForce);
		}
		else {
			this.p.stopAcceleration(input);
		}
	}
	
	// Updates and redraws the world
	public boolean update() {
		boolean finished = false; //whether the game is finished or not
		this.p.stepPosition(this.ws); //update the player coordinates
		
		//check for collisions with DeathWalls
		for (DeathWall dw : this.dws) {
			if (dw.checkCollision(this.p)) {
				finished = true;
			}
		}
		
		//check for collisions with ScoreZones
		for (ScoreZone z : this.szs) {
			if (!z.isUsed() && z.contains(this.p)) {
				p.addScore(z.getScore());
				z.setStatus(true);
				break;
			}
		}
		
		//check for collisions with Goals
		for (Goal g : this.gs) {
			if (g.contains(this.p)) {
				p.addScore(g.getScore());
				finished = true;
			}
		}
		
		this.repaint();
		return finished;
	}
	
	// Resets the world to its initial state
	public void reset() {
		this.p.setAcceleration(0, 0);
		this.p.setVelocity(0, 0);
		this.p.setPosition(Constants.startingX, Constants.startingY);
		this.p.setScore(0);
		for (ScoreZone z : this.szs) {
			z.setStatus(false);
		}
	}
	
	// Adds a new Wall to the world
	public void addWall(Wall w) {
		this.ws.add(w);
	}
	
	// Adds a new DeathWall to the world
	public void addDeathWall(DeathWall dw) {
		this.dws.add(dw);
	}
	
	// Adds a Goal to the world
	public void addGoal(Goal g) {
		this.gs.add(g);
	}
	
	// Adds a ScoreZone to the world
	public void addScoreZone(ScoreZone z) {
		this.szs.add(z);
	}
	
	// Clears the world
	public void clearWorld() {
		this.ws = new ArrayList<Wall>();
		this.dws = new ArrayList<DeathWall>();
		this.gs = new ArrayList<Goal>();
		this.szs = new ArrayList<ScoreZone>();
	}
	
	// Returns the (x,y)-coordinates of the player
	public int[] getPlayerData() {
		return new int[] {this.p.getXPosition(), this.p.getYPosition()};
	}
	
	// Paints the JPanel
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//draw background
		g.setColor(Constants.backgroundColor);
		g.fillRect(0, 0, Constants.screenWidth, Constants.screenHeight);
		
		//draw Walls
		g.setColor(Constants.wallColor);
		for (Wall w : this.ws) {
			g.fillRect(w.x1, w.y1, w.x2 - w.x1, w.y2 - w.y1);
		}
		
		//draw DeathWalls
		g.setColor(Constants.deathWallColor);
		for (DeathWall dw : this.dws) {
			g.fillRect(dw.x1, dw.y1, dw.x2 - dw.x1, dw.y2 - dw.y1);
		}
		
		//draw ScoreZones
		g.setColor(Constants.scoreZoneColor);
		for (ScoreZone z : this.szs) {
			if (!z.isUsed()) {
				g.fillRect(z.x1, z.y1, z.x2 - z.x1, z.y2 - z.y1);
			}
		}
		
		//draw Goals
		g.setColor(Constants.goalColor);
		for (Goal goal : this.gs) {
			g.fillRect(goal.x1, goal.y1, goal.x2 - goal.x1, goal.y2 - goal.y1);
		}
		
		//draw player
		g.setColor(Constants.playerColor);
		g.fillOval(p.getXPosition(), p.getYPosition(), p.getSize(), p.getSize());
	}
	
	// When not in training mode, listens for key events
    private class TAdapter extends KeyAdapter {
    	
        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
	        	case KeyEvent.VK_LEFT:
	        		p.stopAcceleration(Direction.LEFT);
	        		break;
	        	case KeyEvent.VK_UP:
	        		p.stopAcceleration(Direction.UP);
	        		break;
	        	case KeyEvent.VK_RIGHT:
	        		p.stopAcceleration(Direction.RIGHT);
	        		break;
	        	case KeyEvent.VK_DOWN:
	        		p.stopAcceleration(Direction.DOWN);
	        		break;
	        }
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
	        	case KeyEvent.VK_LEFT:
	        		p.accelerate(Direction.LEFT, Constants.accelerationForce);
	        		break;
	        	case KeyEvent.VK_UP:
	        		p.accelerate(Direction.UP, Constants.accelerationForce);
	        		break;
	        	case KeyEvent.VK_RIGHT:
	        		p.accelerate(Direction.RIGHT, Constants.accelerationForce);
	        		break;
	        	case KeyEvent.VK_DOWN:
	        		p.accelerate(Direction.DOWN, Constants.accelerationForce);
	        		break;
	        }
        }
    }
}