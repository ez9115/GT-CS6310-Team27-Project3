package GUI;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class JImageComponent extends JPanel {
	private BufferedImage image;

    public JImageComponent() {
       try {                
          image = ImageIO.read(new File("images/Robinson2.png"));

       } catch (IOException ex) {
            // handle exception...
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);          
    }


}
