package floatingmaze;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * ScreenData --- Contains methods that return the pixel data.
 */
public class ScreenData {
	
	// Gets all the pixel values of a JPanel, downscaled by a factor res
    private static byte[] getPixels(JPanel jpanel, int res) {
    	BufferedImage rescaled = getRescaledImage(jpanel, res);
        return ((DataBufferByte) rescaled.getRaster().getDataBuffer()).getData();
    }
    
    // Returns a byte-array with all the pixels in a given JPanel, in grayscalep
    public static byte[] getAllPixels(JPanel jpanel, int resolution) {
	    byte[] pixels = getPixels(jpanel, resolution); //pixels in format {R,G,B,R,G,B,R,G,B,...}
	    byte[] result = new byte[pixels.length / 3];
	    int k = 0;
	    for (int j = 0; j < pixels.length; j += 3) { //gets all the red pixels
		    result[k] = (byte) (pixels[j] & 0xff); //since the game is grayscaled, this is equivalent to getting grayscaled data
		    k++;
	    }
	    return result;
    }
    
    // Saves the rescaled image as a file, simulating what the machine learning agent sees
    public static void saveRescaledImage(JPanel jpanel, int res) {
        BufferedImage rescaled = getRescaledImage(jpanel, res);
        File outputfile = new File("vision_test.jpg");
        try {
        	ImageIO.write(rescaled, "jpg", outputfile);
        } catch(Exception e) {
        	System.out.println("Could not create file: " + e.getMessage());
        }
    }
    
    // Gets a scaled down BufferedImage of the JPanel
    public static BufferedImage getRescaledImage(JPanel jpanel, int res) {
        BufferedImage img = new BufferedImage(jpanel.getWidth(), jpanel.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage rescaled = new BufferedImage(jpanel.getWidth() / res, jpanel.getHeight() / res, BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = img.getGraphics();
        jpanel.paint(g);
        g = rescaled.createGraphics();
        g.drawImage(img, 0, 0, jpanel.getWidth() / res, jpanel.getWidth() / res, null);
        g.dispose();
        return rescaled;
    }
}