package cs6310.gui.widget.earth;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * Use this class to get a reference to the JPEG image of the earth.
 * 
 * @author Andrew Bernard
 */
public class EarthImage {
	private URL imgURL = this.getClass().getResource("earth-800x400.jpg");
	
	EarthImage() {
		imgURL = this.getClass().getResource("earth-800x400.jpg");
	}
	
	BufferedImage getBufferedImage() {
		BufferedImage earthImage = null;
		try {
      earthImage = ImageIO.read(imgURL);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return earthImage;
	}
}
