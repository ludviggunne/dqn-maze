package floatingmaze;

import java.awt.Color;

/**
 * Constants --- Class defining important environment constants in the game.
 */
public class Constants {
	public static int timeStep = 25; //time interval between frames
	public static double forceScale = 0.5; //pixels per time step per acceleration force unit
	public static double velMax = 5; //maximum (component-wise) speed
	public static int accelerationForce = 1; //the force with which a player is accelerated when pressing a key
	public static int screenWidth = 300; //the width of the window
	public static int screenHeight = 300; //the height of the window
	public static int startingX = 140; //the initial x-position of the player
	public static int startingY = 240; //the initial y-position of the player
	public static int playerSize = 20; //the radius of the player
	public static Color backgroundColor = Color.WHITE; //the background color of the window
	public static Color wallColor = Color.BLACK; //the color of the Walls
	public static Color deathWallColor = new Color(0.3f, 0.3f, 0.3f); //the color of the DeathWalls
	public static Color scoreZoneColor = new Color(0.9f, 0.9f, 0.9f); //the color of the ScoreZones
	public static Color goalColor = Color.DARK_GRAY; //the color of the Goals
	public static Color playerColor = Color.BLACK; //the color of the player
}