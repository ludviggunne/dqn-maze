package floatingmaze;

import java.awt.Dimension;
import java.nio.ByteBuffer;
import javax.swing.JFrame;
import py4j.GatewayServer;

/**
 * Maze --- The base class for launching the game.
 */
public class Maze {
	public JFrame app;
	public World world;
	public TrainingMode training;
	
	public static void main(String[] args) {
		TrainingMode training = TrainingMode.NONE; //set the training mode
		Maze mainProcess = new Maze();
		switch (training) {
			case RL:
				mainProcess.training = training;
			    GatewayServer server = new GatewayServer(mainProcess);
			    server.start();
			    break;
			case NONE:
				mainProcess.startWindow(false);
				break;
		}
	}
	
	// Launches the JFrame window
	public void startWindow(boolean trainingMode) {
		this.app = new JFrame("Levitation Simulation");	
		Player p = new Player(Constants.startingX, Constants.startingY, Constants.playerSize);
		this.world = new World(p, trainingMode);
		
		world.addWall(new Wall(0, 10, 0, 300));
		world.addWall(new Wall(0, 300, 290, 300));
		world.addWall(new Wall(0, 300, 0, 10));
		world.addWall(new Wall(290, 300, 0, 300));
		world.addScoreZone(new ScoreZone(10, 290, 145, 155, 10));
		world.addGoal(new Goal(10, 290, 10, 60, 30));
		
		world.setFocusable(true);
		world.setPreferredSize(new Dimension(Constants.screenWidth, Constants.screenHeight));
		app.getContentPane().add(world);
	    app.pack();
	    app.setLocationRelativeTo(null);
	    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    app.setVisible(true);
	}
	
	// Progress one training step without any inputs
	public byte[] stepWindowTraining(int res) {
		int[] RLStatus = new int[2]; //contains {player score, game finished}
		RLStatus = this.world.trainingStepRL();
		byte[] rewardData = new byte[] {(byte)(RLStatus[0]), (byte)RLStatus[1]}; //convert to bytes
		byte[] pixelData = getPixelData(res); //contains all the pixels of the screen
        ByteBuffer byteBuffer = ByteBuffer.allocate(rewardData.length + pixelData.length);
        byteBuffer.put(rewardData);
        byteBuffer.put(pixelData);
		return byteBuffer.array(); //returns {score, finished, pixel value 1, pixel value 2, ...}
	}
	
	// Progress one training step with inputs
	public byte[] stepWindowTraining(int dir, boolean acc, int res) { //integer direction required for Py4J
		int[] RLStatus = new int[2]; //contains {player score, game finished}
		switch (dir) {
			case 0:
				RLStatus = this.world.trainingStepRL(Direction.UP, acc);
				break;
			case 1:
				RLStatus = this.world.trainingStepRL(Direction.DOWN, acc);
				break;
			case 2:
				RLStatus = this.world.trainingStepRL(Direction.LEFT, acc);
				break;
			case 3:
				RLStatus = this.world.trainingStepRL(Direction.RIGHT, acc);
				break;
		}
		byte[] rewardData = new byte[] {(byte)(RLStatus[0]), (byte)RLStatus[1]}; //convert to bytes
		byte[] pixelData = getPixelData(res); //contains all the pixels of the screen
        ByteBuffer byteBuffer = ByteBuffer.allocate(rewardData.length + pixelData.length);
        byteBuffer.put(rewardData);
        byteBuffer.put(pixelData);
		return byteBuffer.array(); //returns {score, finished, pixel value 1, pixel value 2, ...}
	}
	
	// Gets the (x,y)-position of the player
	public int[] getPlayerData() {
		return this.world.getPlayerData();
	}
	
	// Gets the pixel data in a byte-array
	public byte[] getPixelData(int res) {
		return ScreenData.getAllPixels(this.world, res);
	}
	
	// Returns important game constants. This method is used for Py4J
	public int getConstant(int constant) { //integer value required for Py4J
		switch(constant) {
			case 0:
				return Constants.startingX;
			case 1:
				return Constants.startingY;
			case 2:
				return Constants.screenWidth;
			case 3:
				return Constants.screenHeight;
			case 4:
				return Constants.timeStep;
			default:
				return 0;
		}
	}
	
	// Resets the world to its original state
	public void reset() {
		this.world.reset();
	}
}
