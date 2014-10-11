package GUI;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * A class that demonstrates how to draw temperature grids in Java.
 * Displays a square grid in which the cells are painted with
 *   a random intensity of the Color red.
 * 
 * Modified by Jon to work with the Heated Plate project for cs6310.
 * 
 * @author Spencer Rugaber, January, 2009
 * @author Jonathon Green, September, 2014
 */
@SuppressWarnings("serial")
public class GraphicalRenderer extends JPanel {
	
	private float[][] plateTemps;

	public float[][] getPlateTemps() {
		return plateTemps;
	}

	public void setPlateTemps(float[][] plateTemps) {
		this.plateTemps = plateTemps;
	}

	/**
     * Creates a DrawnGrid, which is a displayable rectangle.  
     * All units are in pixels.
     *
     * @param x X coordinate of the upper left hand corner of the
     *                 rectangle within the containing widget
     * @param y Y coordinate of the upper left hand corner of the
     *                 rectangle within the containing widget
     * @param wh height and width of the square
     */
    public GraphicalRenderer(int x, int y) {
        ulhcX = x;
        ulhcY = y;
        setup(5); // just a default
    }
    
    public void setup(int d){
    	
    	NUMBER_OF_COLUMNS = NUMBER_OF_ROWS = d + 2;
    	
    	CELL_HEIGHT = GRID_SIZE / NUMBER_OF_COLUMNS;
    	CELL_WIDTH = GRID_SIZE / NUMBER_OF_ROWS;

    }

    /**
     * Informs Swing of how much space is needed for drawing.
     * 
     * @return Dimension - the size of the drawing area
     * @override getPreferredSize in JPanel
     * @see javax.swing.JComponent#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        return new Dimension(WINDOW_SIZE, WINDOW_SIZE);
    }
    
    /**
     * Paints one cell of the grid.
     *
     * @param aGraphics Graphics into which painting will be done
     * @param row row number of the grid cell
     * @param col column number of the grid cell
     * @param t intensity of Color red to be painted; a number from 0.0 to 1.0
     */
    private void paintSpot(Graphics aGraphics, int row, int col, float t) {
        int rowPos = ulhcY + row * CELL_HEIGHT;
        int colPos = ulhcX + col * CELL_WIDTH;

        // Overwrite everything that was there previously
        aGraphics.setColor(Color.black);
        aGraphics.fillRect(colPos, rowPos, CELL_WIDTH, CELL_HEIGHT);
        
        // Color in RGB format with green and blue values = 0.0
        aGraphics.setColor(createColor(t));    
        aGraphics.fillRect(colPos, rowPos, CELL_WIDTH, CELL_HEIGHT);
    }
    
    private Color createColor(float temp){
    	if(temp == 0.)
    		return new Color(0.f,0.f,1.f);
    	else
    	{
    		float r = (float) (temp/100.);
    		float g = 0.f;
    		float b = (float) ((100. - temp)/100.);
    		
    		return new Color(r, g, b);
    	}
    }

    /**
     * Informs Swing how to render in terms of subcomponents.
     * @param aGraphics Graphics - Graphs context for drawing
     * @override paintComponent in JPanel
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    protected void paintComponent(Graphics aGraphics) {
        super.paintComponent(aGraphics);
        
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics anotherGraphics = bi.createGraphics();

        for (int i = 0; i < NUMBER_OF_ROWS; i++)
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++)
            	if(plateTemps != null)
            		paintSpot(anotherGraphics, i, j, plateTemps[i][j]);
        
        aGraphics.drawImage(bi, 0, 0, this);
    }
    
    /**
     * Calling routine for DrawnGrid demo
     * @param args unused
     */
//    public static void main(String[] args) {
//        JFrame jf = new JFrame();
//        jf.setTitle("Java Drawing Demo");
//
//        jf.add(new DrawnGrid(BORDER_SIZE, BORDER_SIZE, 
//                             size, size));
//
//        jf.pack();   
//        jf.setVisible(true);        
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
    
    /**
     * Size of the containing window in pixels
     */
    private static int WINDOW_SIZE = 600;
    
    /**
     * Amount of border space around the DrawnGrid in pixels
     */
    private final static int BORDER_SIZE = 50;

    /**
     * Height and width of the DrawnGrid in pixels 
     */
    private final static int GRID_SIZE = WINDOW_SIZE - 2 * BORDER_SIZE;
    
    /**
     * Number of rows of cells in the DrawnGrid 
     */
    private int NUMBER_OF_ROWS;
    
    /**
     * Number of cells in a row 
     */
    private int NUMBER_OF_COLUMNS;
    
    /**
     * The height of a cell in pixels 
     */
    private int CELL_HEIGHT;

    /**
     * The width of a cell in pixels
     */
    private int CELL_WIDTH;

    /**
     * X-coordinate of the upper left hand corner of the rectangle
     *     within the containing widget
     */    
    private int ulhcX;
    
    /**
     * Y-coordinate of the upper left hand corner of the rectangle
     *     within the containing widget
     */
    private int ulhcY;

}