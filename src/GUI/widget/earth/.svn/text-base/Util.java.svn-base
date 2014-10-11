package cs6310.gui.widget.earth;

/**
 * Provides utility functions for geometric calculations related to the earth
 * grid.
 * 
 * @author Andrew Bernard
 */
public abstract class Util {
  
	/**
	 * Computes the circumference of the circle at the given latitude.
	 * 
   * @param latitude in degrees
   * @param earthRadius radius of the earth
   * 
   * @return the circumference of the circle at the given latitude in the units
   * given by <code>earthRadius</code> (i.e. if <code>earthRadius</code> is in 
   * pixels so is the calculated distance)
   */
  public static float getLatitudeCircum(double latitude, double earthRadius) {
    double latRadius = earthRadius * Math.sin(Math.toRadians(90d - latitude));
    return (float) (2d * latRadius * Math.PI);
  }

  /**
   * Computes the area of a trapezoid. All the lengths should be
   * of the same unit of measure.
   * 
   * @param topLength
   * @param bottomLength 
   * @param height
   * 
   * @return the area of the trapezoid
   */
  public static float getTrapezoidArea(double topLength, double bottomLength, double height) {
    return (float) ((.5 * height) * (topLength + bottomLength));
  }

  /**
   * Computes the length of the non-parallel sides of a trapezoid. All the 
   * lengths should be of the same unit of measure.
   * 
   * @param topLength
   * @param bottomLength 
   * @param height
   * 
   * @return the length of the non-parallel sides of the trapezoid
   */
  public static float getTrapezoidSideLen(double topLength, double bottomLength, double height) {
    return (float) Math.sqrt(Math.pow((Math.abs(topLength - bottomLength) / 2), 2) + Math.pow(height, 2));
  }

  /**
   * Computes the distance from a latitudal degree to the equator.
   * 
   * @param latitude in degrees
   * @param earthRadius radius of the earth
   * 
   * @return the distance to the equator in the units given by 
   * <code>earthRadius</code> (i.e. if <code>earthRadius</code> is in pixels so
   * is the calculated distance) 
   */
  public static float getDistToEquator(double latitude, double earthRadius) {
    return (float) (earthRadius * Math.sin(Math.toRadians(latitude)));
  }
  
}
