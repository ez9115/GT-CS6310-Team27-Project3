package cs6310.gui.widget.earth;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * This class is a panel displaying the sun as a circle passing over a 
 * line representing it's path. 360 degrees is the left end point of the line.
 * 
 * @author Andrew Bernard
 */
public class SunDisplay extends JPanel {
  private static final long serialVersionUID = 1L;

  private int pathLength; //see constructor 
  private float initialSunPos = 180f; //see constructor
  private float pixelsPerDegree;
  private int lineOffsetY = 10; //used for positioning the sun on it's line
  private int dimHeight = lineOffsetY+10; //the maximum height of the panel
  
  private Color sunColor = Color.yellow;
  private int sunDiameter = 10; //in pixels - used for drawing the sun
  private float degreePosition = 360f; //the degree position of the sun
  private int pixelPosition; //the pixel position of the sun's path
  
  /**
   * Constructor.
   * @param pathLength the length of the path to draw in pixels
   */
  SunDisplay(int pathLength) {
    init(pathLength);
  }
  
  /**
   * Constructor.
   * @param pathLength the length of the path to draw in pixels
   * @param initialSunPos the initial position of the sun in degrees
   */
  SunDisplay(int pathLength, float initialSunPos) {
    this.initialSunPos = initialSunPos;
    init(pathLength);
  }
  
  private void init(int pathLength) {
  	drawSunPath(pathLength);
    moveSunPosition(this.initialSunPos);
  }
  
  /**
   * This will redraw the path of the sun to the specified length.
   * 
   * @param pathLength in pixels
   */
  void drawSunPath(int pathLength) {
    this.pathLength = pathLength;
    pixelsPerDegree = pathLength / 360f;    
    
    Dimension dim = new Dimension(pathLength, dimHeight);
    setPreferredSize(dim);
    setMaximumSize(dim);
  }
  
  /**
   * Overrides the default paint method for this panel.
   */
  public void paint(Graphics g) {
    //draw the path
    g.setColor(Color.black);
    g.drawLine(0, lineOffsetY, pathLength, lineOffsetY);
    
    //draw the sun
    g.setColor(sunColor);
    g.fillOval(pixelPosition, lineOffsetY/2, sunDiameter, sunDiameter);
  }
  
  /**
   * Moves the sun's position the specified number of degrees.
   * 
   * @param degrees the number of degrees to move the sun
   */
  void moveSunPosition(float degrees) {
    degreePosition -= degrees;
    if(degreePosition < 0) {
      degreePosition += 360;
    }
    
    float pos = pixelsPerDegree * degreePosition;
    pixelPosition = (int)(pos - sunDiameter/2);
  }
  
  /**
   * Resets the sun to its default position.
   */
  void reset() {
    degreePosition = 0f;
    pixelPosition = 0;
    moveSunPosition(this.initialSunPos);
    paint(this.getGraphics());
  }

}
